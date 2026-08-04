package com.admin.marcosaraujo.dev.service.automation;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategorizerService {

    private final GeminiClient geminiClient;

    public CategorizerService(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public String categorize(String title, String content) {
        String prompt = """
               Analise o título e o conteúdo da notícia a seguir e classifique-a em EXATAMENTE UMA das categorias abaixo.
                
                             CATEGORIAS PERMITIDAS:
                             - Economia
                             - Política
                             - Tecnologia
                             - Geral
                
                             REGRA: Responda APENAS com o nome exato da categoria (uma única palavra), sem pontuação ou introdução.
                
                             Título: %s
                             Conteúdo: %s
               """.formatted(title, content);

        String category = geminiClient.sendPrompt(prompt).trim();

        if (!List.of("Economia", "Política", "Tecnologia").contains(category)) {
            return "Geral";
        }
        return category;
    }

}

