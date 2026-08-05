// 1. Utilitário do CSRF para chamadas POST/DELETE no Spring Security
function getCsrfHeader() {
    const headerMeta = document.querySelector("meta[name='_csrf_header']");
    const tokenMeta = document.querySelector("meta[name='_csrf']");
    if (headerMeta && tokenMeta) {
        return { [headerMeta.content]: tokenMeta.content };
    }
    return {};
}

// 2. Utilitários Globais do Modal de IA
function abrirModal(titulo, conteudo, showApproveButton = false) {
    document.getElementById('modal-title').innerText = titulo;
    document.getElementById('modal-body').innerText = conteudo;

    const btnApprove = document.getElementById('btnAprovarBlog');
    if (btnApprove) {
        if (showApproveButton) {
            btnApprove.classList.remove('hidden');
            btnApprove.disabled = false;
            btnApprove.innerText = '✅ Aprovar Conteúdo';
        } else {
            btnApprove.classList.add('hidden');
        }
    }

    document.getElementById('ai-modal').classList.remove('hidden');
}

function fecharModal() {
    document.getElementById('ai-modal').classList.add('hidden');
}

function copiarConteudoModal() {
    const text = document.getElementById('modal-body').innerText;
    navigator.clipboard.writeText(text);
    alert('Conteúdo copiado para a área de transferência!');
}

// 3. Auxiliar para extrair a resposta seja JSON ou Texto simples do Spring
async function extrairConteudoResposta(res, campoJson) {
    const contentType = res.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
        const data = await res.json();
        return data[campoJson] || JSON.stringify(data, null, 2);
    }
    return await res.text();
}