// Variáveis exclusivas do módulo Inbox
let currentCategory = 'TODAS';
let currentItemId = null;
let itemParaDescartarId = null;

document.addEventListener('DOMContentLoaded', () => {
    carregarNoticias('TODAS');
});

// Utility: Formatar data e hora publicadas
function formatarDataPublicacao(dataIso) {
    if (!dataIso) return null;
    try {
        const data = new Date(dataIso);
        if (isNaN(data.getTime())) return null;

        return data.toLocaleString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    } catch (e) {
        return null;
    }
}

// Alternar abas de categoria
function carregarPorCategoria(categoria, btnElement) {
    currentCategory = categoria;

    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.className = 'tab-btn px-3 py-1.5 rounded-lg text-gray-500 dark:text-zinc-400 hover:bg-gray-200/50 dark:hover:bg-zinc-800 transition font-mono text-xs';
    });
    btnElement.className = 'tab-btn px-3 py-1.5 rounded-lg bg-black dark:bg-zinc-100 text-white dark:text-black font-semibold shadow-sm transition font-mono text-xs';

    carregarNoticias(categoria);
}

// Requisição das notícias no Spring Boot
async function carregarNoticias(categoria) {
    const feed = document.getElementById('news-feed');
    feed.innerHTML = `<div class="text-center py-8 font-mono text-xs text-gray-400">Buscando notícias...</div>`;

    try {
        const response = await fetch(`/api/admin/curadoria/categoria/${categoria}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

        const items = await response.json();

        const countPending = document.getElementById('count-pending');
        if (countPending) countPending.innerText = items.length;

        renderizarCards(items);
    } catch (error) {
        console.error("Erro ao carregar notícias:", error);
        feed.innerHTML = `<div class="text-center py-8 font-mono text-xs text-red-500">Erro ao carregar matérias do servidor Java.</div>`;
    }
}

// Renderização dos cards de notícia
function renderizarCards(items) {
    const feed = document.getElementById('news-feed');

    if (!items || items.length === 0) {
        feed.innerHTML = `<div class="text-center py-8 font-mono text-xs text-gray-400">Nenhuma matéria encontrada nesta categoria.</div>`;
        return;
    }

    feed.innerHTML = items.map(item => {
        const rawContentText = item.rawContent || item.raw_content || item.content || '';
        const authorText = item.author || item.author_name || null;
        const formattedDate = formatarDataPublicacao(item.publishedAt || item.published_at);

        return `
        <article class="relative p-5 rounded-xl bg-white dark:bg-zinc-900 border border-gray-200 dark:border-zinc-800 shadow-sm space-y-3 overflow-visible">

            <!-- Botão de Descarte -->
            <button onclick="confirmarDescarte(${item.id})"
                    title="Descartar notícia"
                    class="absolute top-3 right-3 z-10 p-1.5 rounded-full bg-gray-100 dark:bg-zinc-800 text-gray-400 hover:text-red-600 dark:hover:text-red-400 hover:bg-red-50 dark:hover:bg-red-950/40 transition">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
            </button>

            <!-- Imagem de Capa -->
            ${(item.imageUrl || item.image_url) ? `
                <div class="w-full h-44 -mt-5 -mx-5 mb-3 overflow-hidden rounded-t-xl bg-gray-100 dark:bg-zinc-800 border-b border-gray-100 dark:border-zinc-800">
                    <img src="${item.imageUrl || item.image_url}" alt="${item.title}" class="w-full h-full object-cover hover:scale-105 transition duration-300" onerror="this.parentElement.style.display='none'">
                </div>
            ` : ''}

            <!-- Cabeçalho do Card: Fonte, Categoria, Autor e Data -->
            <div class="flex flex-wrap items-center justify-between text-xs font-mono text-gray-400 dark:text-zinc-500 gap-y-1 pr-6">
                <div class="flex flex-wrap items-center gap-2">
                    <span class="px-2 py-0.5 rounded bg-blue-50 dark:bg-blue-950/50 text-blue-600 dark:text-blue-400 font-semibold">
                        ${item.sourceName || item.source_name || 'Fonte'}
                    </span>
                    <span>•</span>
                    <span class="px-2 py-0.5 rounded bg-gray-100 dark:bg-zinc-800 text-gray-600 dark:text-zinc-300 font-medium">
                        ${item.category || 'Geral'}
                    </span>
                    ${authorText ? `
                        <span>•</span>
                        <span class="text-gray-500 dark:text-zinc-400 font-medium">👤 ${authorText}</span>
                    ` : ''}
                    ${formattedDate ? `
                        <span>•</span>
                        <span class="text-gray-400 dark:text-zinc-500">🕒 ${formattedDate}</span>
                    ` : ''}
                </div>
                <button onclick="executarCategorizacao(${item.id})" class="text-[11px] text-purple-600 dark:text-purple-400 hover:underline">
                    🤖 Triar Categoria
                </button>
            </div>

            <!-- Título com Visualizador Hover do raw_content -->
            <div class="relative group inline-block w-full">
                <h2 class="text-base font-bold text-gray-900 dark:text-zinc-100 leading-snug cursor-pointer hover:text-blue-600 dark:hover:text-blue-400 transition">
                    ${item.title || 'Sem título'}
                </h2>

                <!-- Modal Tooltip no Hover com raw_content completo -->
                ${rawContentText ? `
                    <div class="opacity-0 pointer-events-none group-hover:opacity-100 group-hover:pointer-events-auto transition-all duration-200 absolute left-0 top-full mt-2 w-full max-w-2xl bg-white dark:bg-zinc-950 text-gray-800 dark:text-zinc-200 p-4 rounded-xl shadow-2xl border border-gray-200 dark:border-zinc-700 z-50 text-xs font-sans max-h-72 overflow-y-auto leading-relaxed">
                        <div class="font-mono text-[10px] text-gray-400 uppercase tracking-wider mb-2 border-b border-gray-100 dark:border-zinc-800 pb-1 flex justify-between">
                            <span>📄 Conteúdo Bruto (raw_content)</span>
                            <span>Passe o mouse para ler</span>
                        </div>
                        <p class="whitespace-pre-wrap">${rawContentText}</p>
                    </div>
                ` : ''}
            </div>

            <!-- Resumo / Subtítulo da Matéria -->
            <p class="text-sm text-gray-600 dark:text-zinc-300 leading-relaxed line-clamp-2">
                ${item.subtitle || item.description || item.summary || item.content || 'Sem resumo disponível.'}
            </p>

            <!-- Rodapé e Ações das Esteiras de IA -->
            <div class="pt-3 border-t border-gray-100 dark:border-zinc-800/60 flex flex-wrap items-center justify-between gap-2">
                <a href="${item.url}" target="_blank" rel="noopener noreferrer" class="text-xs font-mono text-gray-400 hover:text-black dark:hover:text-white transition">
                    Ver Original ↗
                </a>

                <div class="flex flex-wrap items-center gap-1.5">
                    <button onclick="gerarBlog(${item.id})" class="px-2.5 py-1 text-[11px] font-mono font-medium bg-emerald-50 dark:bg-emerald-950/40 text-emerald-600 dark:text-emerald-400 rounded-md border border-emerald-500/20 hover:bg-emerald-100 transition">
                        📝 Blog (HTML)
                    </button>
                    <button onclick="gerarYouTube(${item.id})" class="px-2.5 py-1 text-[11px] font-mono font-medium bg-red-50 dark:bg-red-950/40 text-red-600 dark:text-red-400 rounded-md border border-red-500/20 hover:bg-red-100 transition">
                        🎬 Roteiro YT
                    </button>
                    <button onclick="gerarLinkedIn(${item.id})" class="px-2.5 py-1 text-[11px] font-mono font-medium bg-blue-50 dark:bg-blue-950/40 text-blue-600 dark:text-blue-400 rounded-md border border-blue-500/20 hover:bg-blue-100 transition">
                        💼 Post LinkedIn
                    </button>
                </div>
            </div>
        </article>
    `}).join('');
}

// Modal de Descarte
function confirmarDescarte(id) {
    itemParaDescartarId = id;
    document.getElementById('modal-descarte').classList.remove('hidden');
}

function fecharModalDescarte() {
    itemParaDescartarId = null;
    document.getElementById('modal-descarte').classList.add('hidden');
}

async function executarExclusao() {
    if (!itemParaDescartarId) return;

    try {
        const res = await fetch(`/api/admin/curadoria/${itemParaDescartarId}`, {
            method: 'DELETE',
            headers: { ...getCsrfHeader() }
        });
        if (res.ok) {
            fecharModalDescarte();
            carregarNoticias(currentCategory);
        } else {
            alert('Erro ao descartar item.');
        }
    } catch (e) {
        alert('Erro de conexão ao excluir.');
    }
}

// Esteiras de IA
async function executarCategorizacao(id) {
    try {
        const res = await fetch(`/api/admin/curadoria/${id}/categorize`, {
            method: 'POST',
            headers: { ...getCsrfHeader() }
        });
        if (res.ok) carregarNoticias(currentCategory);
    } catch (e) {
        console.error('Erro na categorização:', e);
    }
}

async function gerarBlog(id) {
    currentItemId = id;
    abrirModal('Gerando Artigo para Blog...', 'Aguarde a resposta do Gemini...', false);
    try {
        const res = await fetch(`/api/admin/curadoria/${id}/generate-blog`, {
            method: 'POST',
            headers: { ...getCsrfHeader() }
        });
        if (!res.ok) throw new Error(`HTTP Error: ${res.status}`);
        const conteudo = await extrairConteudoResposta(res, 'blogContent');
        abrirModal('Artigo de Blog Gerado (HTML)', conteudo, true);
    } catch (e) {
        console.error(e);
        abrirModal('Erro', 'Falha ao conectar com o serviço de IA.', false);
    }
}

async function gerarYouTube(id) {
    currentItemId = id;
    abrirModal('Gerando Roteiro para YouTube...', 'Aguarde a resposta do Gemini...', false);
    try {
        const res = await fetch(`/api/admin/curadoria/${id}/generate-youtube`, {
            method: 'POST',
            headers: { ...getCsrfHeader() }
        });
        if (!res.ok) throw new Error(`HTTP Error: ${res.status}`);
        const conteudo = await extrairConteudoResposta(res, 'youtubeScript');
        abrirModal('Roteiro Estruturado YouTube', conteudo, false);
    } catch (e) {
        console.error(e);
        abrirModal('Erro', 'Falha ao conectar com o serviço de IA.', false);
    }
}

async function gerarLinkedIn(id) {
    currentItemId = id;
    abrirModal('Gerando Post do LinkedIn...', 'Aguarde a resposta do Gemini...', false);
    try {
        const res = await fetch(`/api/admin/curadoria/${id}/generate-linkedin`, {
            method: 'POST',
            headers: { ...getCsrfHeader() }
        });
        if (!res.ok) throw new Error(`HTTP Error: ${res.status}`);
        const conteudo = await extrairConteudoResposta(res, 'linkedinContent');
        abrirModal('Post Formatado para LinkedIn', conteudo, false);
    } catch (e) {
        console.error(e);
        abrirModal('Erro', 'Falha ao conectar com o serviço de IA.', false);
    }
}

function aprovarArtigoBlog() {
    if (!currentItemId) return;

    const btn = document.getElementById('btnAprovarBlog');
    btn.disabled = true;
    btn.innerText = '⏳ Salvando...';

    fetch(`/api/admin/curadoria/${currentItemId}/approve-blog`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            ...getCsrfHeader()
        }
    })
    .then(response => {
        if (!response.ok) throw new Error('Falha ao aprovar artigo');
        return response.text();
    })
    .then(() => {
        alert('🎉 Artigo aprovado com sucesso!');
        fecharModal();
        carregarNoticias(currentCategory);
    })
    .catch(error => {
        console.error(error);
        alert('❌ Erro ao aprovar conteúdo');
        btn.disabled = false;
        btn.innerText = '✅ Aprovar Conteúdo';
    });
}