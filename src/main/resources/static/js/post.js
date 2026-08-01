document.addEventListener("DOMContentLoaded", function() {
    // 1. Inicializa o EasyMDE Editor
    const markdownTextarea = document.getElementById('markdown-editor');
    let easymde = null;

    if (markdownTextarea) {
        easymde = new EasyMDE({
            element: markdownTextarea,
            spellChecker: false,
            forceSync: true,
            autosave: { enabled: false },
            placeholder: "Escreva seu conteúdo em Markdown aqui...",
            renderingConfig: {
                singleLineBreaks: true,
                codeSyntaxHighlighting: true
            },
            toolbar: [
                "bold", "italic", "heading", "|",
                "quote", "unordered-list", "ordered-list", "|",
                "link", "code", "table", "|",
                "preview", "side-by-side", "fullscreen", "|",
                "guide"
            ]
        });

        // Ativa o modo Lado a Lado (Side-by-Side)
        setTimeout(() => {
            if (!easymde.isSideBySideActive()) {
                easymde.toggleSideBySide();
            }
        }, 200);

        // Sincroniza o conteúdo ao submeter o formulário
        const postForm = document.getElementById('postForm');
        if (postForm) {
            postForm.addEventListener('submit', function() {
                markdownTextarea.value = easymde.value();
            });
        }
    }

    // 2. Auto-gerador de Slug em tempo real
    const titleInput = document.getElementById('title');
    const slugInput = document.getElementById('slug');

    if (titleInput && slugInput) {
        titleInput.addEventListener('input', () => {
            if (!slugInput.dataset.userEdited) {
                slugInput.value = generateSlug(titleInput.value);
            }
        });

        slugInput.addEventListener('input', () => {
            slugInput.dataset.userEdited = "true";
        });
    }

    function generateSlug(str) {
        return str
            .toLowerCase()
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .replace(/[^a-z0-9 -]/g, "")
            .trim()
            .replace(/\s+/g, "-")
            .replace(/-+/g, "-");
    }

    // 3. Gerenciamento do Estado de Rascunho x Agendamento
    const draftCheckbox = document.getElementById('draft');
    const scheduleCard = document.getElementById('scheduleCard');

    function toggleScheduleState() {
        if (draftCheckbox && scheduleCard) {
            if (draftCheckbox.checked) {
                scheduleCard.style.opacity = '0.4';
                scheduleCard.style.pointerEvents = 'none';
            } else {
                scheduleCard.style.opacity = '1';
                scheduleCard.style.pointerEvents = 'auto';
            }
        }
    }

    if (draftCheckbox) {
        draftCheckbox.addEventListener('change', toggleScheduleState);
        toggleScheduleState();
    }

    // 4. Funções para os Botões de Atalho de Agendamento
    function formatToDateTimeInput(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        return `${year}-${month}-${day}T${hours}:${minutes}`;
    }

    window.setSchedule = function(hoursAhead) {
        const date = new Date();
        date.setHours(date.getHours() + hoursAhead);
        const publishedInput = document.getElementById('publishedAt');
        if (publishedInput) publishedInput.value = formatToDateTimeInput(date);
    };

    window.setNextMonday = function() {
        const date = new Date();
        date.setDate(date.getDate() + ((1 + 7 - date.getDay()) % 7 || 7));
        date.setHours(9, 0, 0, 0);
        const publishedInput = document.getElementById('publishedAt');
        if (publishedInput) publishedInput.value = formatToDateTimeInput(date);
    };
});