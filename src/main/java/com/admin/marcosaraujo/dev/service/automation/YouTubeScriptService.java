package com.admin.marcosaraujo.dev.service.automation;

import org.springframework.stereotype.Service;

@Service
public class YouTubeScriptService {

    private final GeminiClient geminiClient;

    public YouTubeScriptService(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public String generateScript(String title, String content) {
        String prompt = """
    Você é um roteirista sênior do YouTube especializado em canais de curiosidades, notícias e tecnologia com linguagem extremamente acessível.
    Com base na matéria fornecida, crie um roteiro de vídeo dinâmico, envolvente e divertido.

    PERFIL DO APRESENTADOR (MARCOS ARAÚJO):
    - NÃO fale como um programador ou especialista técnico. Fale como um criador de conteúdo normal, curioso e antenado.
    - O objetivo principal é explicar a notícia para quem NÃO entendeu nada, usando linguagem simples, direta e analogias da vida real.
    - Tom de voz: Descontraído, levemente sarcástico, humor sutil, bem-humorado e objetivo.
    - Estilo de fala: Ritmo rápido, dinâmico e focado em prender a atenção (retenção de vídeo).

    REGRAS DE FORMATAÇÃO:
    - Retorne ESTRITAMENTE um JSON válido.
    - NÃO inclua explicações, comentários fora do JSON ou marcações como ```json no início ou fim.

    ESTRUTURA JSON ESPERADA:
    {
      "hook_5s": "Frase chocante, sarcástica ou intrigante para os primeiros 5 segundos",
      "context": "Resumo simples da notícia para uma criança de 12 anos entender",
      "curiosities": ["Curiosidade ou bastidor 1 sobre o tema", "Curiosidade ou impacto prático 2"],
      "b_roll_suggestions": ["Sugestão de cena/imagem/meme 1", "Sugestão de cena/imagem/meme 2"],
      "script_body": [
        {"speaker": "Apresentador", "text": "Fala do apresentador com piada leve ou analogia simples...", "visual_cue": "Efeito sonoro, zoom na cara ou imagem na tela"}
      ],
      "call_to_action": "Chamada divertida para se inscrever e comentar no vídeo"
    }

    Título Original: %s
    Conteúdo Original: %s
    """.formatted(title, content);

        return geminiClient.sendPrompt(prompt);
    }
}