package com.admin.marcosaraujo.dev.service.automation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class GeminiClient {

    private static final Logger log = LoggerFactory.getLogger(GeminiClient.class);
    private final RestClient restClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiClient() {
        this.restClient = RestClient.create();
    }

    public String sendPrompt(String promptText) {
        log.info("🤖 [GeminiClient] Preparando requisição para a API do Gemini...");

        // Endpoint atualizado v1beta com o modelo Gemini 2.5 Flash
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

        // Estrutura do JSON esperada pela API do Gemini
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", promptText)
                                )
                        )
                )
        );

        try {
            log.info("🚀 [GeminiClient] Enviando requisição para: {}", url);

            Map response = restClient.post()
                    .uri(url)
                    .header("x-goog-api-key", apiKey) // Envia a sua chave AQ.Ab8... no header seguro
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            return extractTextFromResponse(response);

        } catch (Exception e) {
            log.error("❌ [GeminiClient] Erro ao chamar API do Gemini: {}", e.getMessage(), e);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    private String extractTextFromResponse(Map response) {
        try {
            if (response == null || !response.containsKey("candidates")) {
                log.warn("⚠️ [GeminiClient] Resposta sem candidatos válidos.");
                return "";
            }

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            if (candidates.isEmpty()) return "";

            Map<String, Object> firstCandidate = candidates.get(0);
            Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

            if (parts != null && !parts.isEmpty()) {
                return (String) parts.get(0).get("text");
            }
        } catch (Exception e) {
            log.error("❌ [GeminiClient] Falha ao extrair texto do JSON de resposta: {}", e.getMessage());
        }
        return "";
    }
}