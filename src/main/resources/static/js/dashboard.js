
document.addEventListener("DOMContentLoaded", function() {
    // 1. Renderiza os Popovers em Markdown
    const previewContainers = document.querySelectorAll('.preview-container');
    previewContainers.forEach(container => {
        const template = container.querySelector('.raw-markdown');
        const targetDiv = container.querySelector('.preview-content');

        if (template && targetDiv) {
            const rawContent = template.innerHTML.trim();
            if (rawContent) {
                targetDiv.innerHTML = marked.parse(rawContent);
            } else {
                targetDiv.innerHTML = '<p class="text-gray-400 italic text-xs">Sem conteúdo disponível para preview.</p>';
            }
        }
    });

    // 2. Busca e Filtro em Tempo Real
    const searchInput = document.getElementById('searchInput');
    const filterBtns = document.querySelectorAll('.filter-btn');
    const postItems = document.querySelectorAll('.post-item');
    const noResults = document.getElementById('noResults');

    let currentFilter = 'all';

    function applyFilters() {
        const searchTerm = searchInput ? searchInput.value.toLowerCase().trim() : '';
        let visibleCount = 0;

        postItems.forEach(item => {
            const title = item.getAttribute('data-title') || '';
            const slug = item.getAttribute('data-slug') || '';
            const status = item.getAttribute('data-status') || '';

            const matchesSearch = title.includes(searchTerm) || slug.includes(searchTerm);
            const matchesFilter = (currentFilter === 'all') || (status === currentFilter);

            if (matchesSearch && matchesFilter) {
                item.style.display = 'flex';
                visibleCount++;
            } else {
                item.style.display = 'none';
            }
        });

        if (noResults) {
            if (visibleCount === 0 && postItems.length > 0) {
                noResults.classList.remove('hidden');
            } else {
                noResults.classList.add('hidden');
            }
        }
    }

    if (searchInput) {
        searchInput.addEventListener('input', applyFilters);
    }

    filterBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            filterBtns.forEach(b => {
                b.classList.remove('active', 'bg-black', 'dark:bg-zinc-100', 'text-white', 'dark:text-black');
                b.classList.add('text-gray-500', 'dark:text-zinc-400', 'hover:text-black', 'dark:hover:text-zinc-100', 'hover:bg-gray-100', 'dark:hover:bg-zinc-800');
            });

            btn.classList.add('active', 'bg-black', 'dark:bg-zinc-100', 'text-white', 'dark:text-black');
            btn.classList.remove('text-gray-500', 'dark:text-zinc-400', 'hover:text-black', 'dark:hover:text-zinc-100', 'hover:bg-gray-100', 'dark:hover:bg-zinc-800');

            currentFilter = btn.getAttribute('data-filter');
            applyFilters();
        });
    });

    // 3. Copiar Link
    const copyBtns = document.querySelectorAll('.copy-btn');
    copyBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const urlToCopy = this.getAttribute('data-url');
            const textSpan = this.querySelector('.btn-text');

            navigator.clipboard.writeText(urlToCopy).then(() => {
                const originalText = textSpan.textContent;
                textSpan.textContent = 'Copiado! ✓';
                this.classList.add('text-green-600', 'dark:text-green-400', 'font-bold');

                setTimeout(() => {
                    textSpan.textContent = originalText;
                    this.classList.remove('text-green-600', 'dark:text-green-400', 'font-bold');
                }, 2000);
            }).catch(err => {
                console.error('Erro ao copiar link: ', err);
            });
        });
    });

    // 4. Cronômetro Regressivo
    function updateCountdowns() {
        const scheduledItems = document.querySelectorAll('.post-item[data-status="scheduled"]');

        scheduledItems.forEach(item => {
            const publishDateStr = item.getAttribute('data-publish-date');
            const timerElement = item.querySelector('.countdown-timer');

            if (!publishDateStr || !timerElement) return;

            const targetDate = new Date(publishDateStr).getTime();
            const now = new Date().getTime();
            const diff = targetDate - now;

            if (diff <= 0) {
                timerElement.innerText = "Publicando agora...";
                setTimeout(() => window.location.reload(), 3000);
                return;
            }

            const days = Math.floor(diff / (1000 * 60 * 60 * 24));
            const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((diff % (1000 * 60)) / 1000);

            let text = "";
            if (days > 0) text += `${days}d `;
            if (hours > 0 || days > 0) text += `${hours}h `;
            text += `${minutes}m ${seconds}s`;

            timerElement.innerText = text;
        });
    }

    setInterval(updateCountdowns, 1000);
    updateCountdowns();
});