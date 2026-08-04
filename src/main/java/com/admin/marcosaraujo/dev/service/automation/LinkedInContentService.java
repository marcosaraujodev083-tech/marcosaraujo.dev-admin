package com.admin.marcosaraujo.dev.service.automation;

import org.springframework.stereotype.Service;

@Service
public class LinkedInContentService {

    private final GeminiClient geminiClient;

    public LinkedInContentService(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public  String generatedPost(String title, String context) {
        String prompt = """
            Você é um estrategista de conteúdo e copywriting para o LinkedIn.
            Transforme a notícia abaixo em um post com alto potencial de engajamento no LinkedIn.
            
            REGRAS DE FORMATO:
            - Primeira linha impactante (hook) que faça o leitor clicar em "ver mais".
            - Parágrafos curtos (1 a 2 frases no máximo) com espaçamento duplo.
            - Extraia 3 lições ou insights estratégicos de mercado a partir do fato.
            - Tom profissional, porém humanizado e provocativo.
            - Termine com uma pergunta aberta para gerar comentários.
            - Adicione de 3 a 5 hashtags relevantes no final.
            
            Matéria: %s - %s
            """.formatted(title, context);

        return geminiClient.sendPrompt(prompt);
    }
}