/**
 * curadoria-realtime.js
 * Gerencia a conexão WebSocket com o Supabase Realtime e a filtragem por Categorias (Tech, Economia, etc.).
 */

let supabaseClient = null;
let categoriaAtiva = 'TODAS'; // 'TODAS', 'TECH', 'ECONOMY'

document.addEventListener('DOMContentLoaded', () => {
    inicializarSupabaseRealtime();
    atualizarContadores();
});

/**
 * Inicializa a conexão com o Supabase e escuta inserts na tabela inbox_news
 */
function inicializarSupabaseRealtime() {
    const feed = document.getElementById('news-feed');
    if (!feed) return;

    const supabaseUrl = feed.dataset.supabaseUrl;
    const supabaseKey = feed.dataset.supabaseKey;

    if (supabaseUrl && supabaseKey && typeof supabase !== 'undefined') {
        supabaseClient = supabase.createClient(supabaseUrl, supabaseKey);

        supabaseClient
            .channel('realtime_curadoria')
            .on(
                'postgres_changes',
                {
                    event: 'INSERT',
                    schema: 'public',
                    table: 'inbox_news'
                },
                (payload) => {
                    console.log('⚡ Nova notícia recebida via WebSocket:', payload.new);
                    adicionarNovoCardNaTela(payload.new);
                    atualizarContadores();
                }
            )
            .subscribe((status) => {
                const statusBadge = document.getElementById('realtime-status');
                if (statusBadge && status === 'SUBSCRIBED') {
                    statusBadge.classList.remove('hidden');
                }
            });
    }
}

/**
 * Filtra a exibição dos cards com base na aba clicada
 */
function filtrarPorCategoria(categoria, elementoBotao) {
    categoriaAtiva = categoria.toUpperCase();

    // 1. Atualiza o estilo visual dos botões de abas
    const botoes = document.querySelectorAll('.tab-btn');
    botoes.forEach(btn => {
        btn.classList.remove('bg-gray-900', 'dark:bg-zinc-100', 'text-white', 'dark:text-zinc-900', 'font-semibold', 'shadow-sm');
        btn.classList.add('text-gray-500', 'dark:text-zinc-400', 'hover:bg-gray-200/50', 'dark:hover:bg-zinc-800');
    });

    elementoBotao.classList.remove('text-gray-500', 'dark:text-zinc-400', 'hover:bg-gray-200/50', 'dark:hover:bg-zinc-800');
    elementoBotao.classList.add('bg-gray-900', 'dark:bg-zinc-100', 'text-white', 'dark:text-zinc-900', 'font-semibold', 'shadow-sm');

    // 2. Oculta ou exibe os cards conforme a categoria selecionada
    const cards = document.querySelectorAll('.news-card');
    cards.forEach(card => {
        const catCard = (card.dataset.category || '').toUpperCase();

        if (categoriaAtiva === 'TODAS' || catCard === categoriaAtiva) {
            card.classList.remove('hidden');
        } else {
            card.classList.add('hidden');
        }
    });
}

/**
 * Renderiza um novo card vindo do WebSocket ou Banco
 */
function adicionarNovoCardNaTela(noticia) {
    const feed = document.getElementById('news-feed');
    if (!feed) return;

    const catNormalizada = (noticia.category || 'TECH').toUpperCase();
    const isEconomia = catNormalizada === 'ECONOMY' || catNormalizada === 'ECONOMIA';

    // Define as cores e badges baseados na categoria
    const badgeCor = isEconomia
        ? 'bg-emerald-50 dark:bg-emerald-950/50 text-emerald-600 dark:text-emerald-400'
        : 'bg-blue-50 dark:bg-blue-950/50 text-blue-600 dark:text-blue-400';

    const catLabel = isEconomia ? 'Economia & Mercado' : 'Tecnologia';

    // Verifica se deve nascer oculto caso o usuário esteja em outra aba no momento
    const deveEsconder = (categoriaAtiva !== 'TODAS' && categoriaAtiva !== catNormalizada);
    const hiddenClass = deveEsconder ? 'hidden' : '';

    const cardHTML = `
        <article class="news-card p-5 rounded-xl bg-white dark:bg-[#18181b] border border-gray-200/80 dark:border-zinc-800 shadow-sm transition duration-200 ${hiddenClass}"
                 data-category="${catNormalizada}">

            <div class="flex items-center justify-between text-xs font-mono text-gray-400 dark:text-zinc-500 mb-2.5">
                <div class="flex items-center space-x-2">
                    <span class="px-2 py-0.5 rounded ${badgeCor} font-semibold">
                        ${escapeHtml(noticia.source_name || 'Fonte')}
                    </span>
                    <span>•</span>
                    <span class="px-2 py-0.5 rounded bg-gray-100 dark:bg-zinc-800 text-gray-600 dark:text-zinc-300 font-medium">
                        ${catLabel}
                    </span>
                </div>
                <span class="text-xs font-bold text-blue-600 dark:text-blue-400 font-mono">NEW ★ agora</span>
            </div>

            <h2 class="text-base font-bold text-gray-900 dark:text-zinc-100 leading-snug mb-2">
                ${escapeHtml(noticia.title)}
            </h2>

            <p class="text-sm text-gray-600 dark:text-zinc-300 leading-relaxed mb-4">
                ${escapeHtml(noticia.summary)}
            </p>

            <div class="flex items-center justify-between pt-3 border-t border-gray-100 dark:border-zinc-800/60">
                <a href="${escapeHtml(noticia.original_url || '#')}" target="_blank" rel="noopener noreferrer" class="text-xs font-mono text-gray-400 dark:text-zinc-500 hover:text-gray-900 dark:hover:text-zinc-200 transition">
                    Ver fonte original ↗
                </a>
                <div class="flex items-center space-x-2">
                    <button type="button" onclick="descartarNoticia(this)" class="px-3 py-1.5 text-xs font-mono font-medium text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-950/40 rounded-lg transition">
                        Descartar
                    </button>
                    <button type="button" onclick="aprovarEVirarPost(this)" class="px-3 py-1.5 text-xs font-mono font-semibold bg-blue-600 hover:bg-blue-700 text-white rounded-lg shadow-sm transition">
                        Converter em Post →
                    </button>
                </div>
            </div>
        </article>
    `;

    feed.insertAdjacentHTML('afterbegin', cardHTML);
}

/**
 * Ações de interface
 */
function descartarNoticia(btn) {
    const card = btn.closest('.news-card');
    if (!card) return;

    card.style.opacity = '0.2';
    card.style.transform = 'scale(0.97)';
    card.style.transition = 'all 0.2s ease';

    setTimeout(() => {
        card.remove();
        atualizarContadores();
    }, 200);
}

function aprovarEVirarPost(btn) {
    const card = btn.closest('.news-card');
    const titulo = card ? card.querySelector('h2').innerText : '';
    alert(`Notícia aprovada! Redirecionando para o editor: "${titulo}"`);
}

/**
 * Atualiza dinamicamente a contagem das abas e métricas
 */
function atualizarContadores() {
    const cards = document.querySelectorAll('.news-card');
    let total = cards.length;
    let techCount = 0;
    let economyCount = 0;

    cards.forEach(card => {
        const cat = (card.dataset.category || '').toUpperCase();
        if (cat === 'TECH') techCount++;
        if (cat === 'ECONOMY' || cat === 'ECONOMIA') economyCount++;
    });

    const elTotal = document.getElementById('count-total');
    const elTech = document.getElementById('count-tech');
    const elEconomy = document.getElementById('count-economy');
    const elPending = document.getElementById('count-pending');

    if (elTotal) elTotal.innerText = total;
    if (elTech) elTech.innerText = techCount;
    if (elEconomy) elEconomy.innerText = economyCount;
    if (elPending) elPending.innerText = total;
}

function escapeHtml(text) {
    if (!text) return '';
    return text
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}