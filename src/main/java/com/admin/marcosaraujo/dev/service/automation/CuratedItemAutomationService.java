package com.admin.marcosaraujo.dev.service.automation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CuratedItemAutomationService {

    private static final Logger log = LoggerFactory.getLogger(CuratedItemAutomationService.class);

    private final CategorizerService categorizerService;
    private final BlogRedactorService blogRedactorService;
    private final YouTubeScriptService youtubeScriptService;
    private final LinkedInContentService linkedInContentService;

    public CuratedItemAutomationService(
            CategorizerService categorizerService,
            BlogRedactorService blogRedactorService,
            YouTubeScriptService youtubeScriptService,
            LinkedInContentService linkedInContentService) {
        this.categorizerService = categorizerService;
        this.blogRedactorService = blogRedactorService;
        this.youtubeScriptService = youtubeScriptService;
        this.linkedInContentService = linkedInContentService;
    }

    public String processCategorization(String title, String content) {
        log.info("🤖 [IA Categorizer] Iniciando triagem. Título: '{}'", title);
        try {
            String category = categorizerService.categorize(title, content);
            log.info("✅ [IA Categorizer] Categoria atribuída: '{}'", category);
            return category;
        } catch (Exception e) {
            log.error("❌ [IA Categorizer] Erro ao processar categoria:", e);
            throw new RuntimeException("Falha na chamada da IA (Categorização): " + e.getMessage(), e);
        }
    }

    public String processBlogDraft(String title, String content) {
        log.info("📝 [IA Blog] Disparando requisição Gemini para Artigo. Título: '{}'", title);
        try {
            String result = blogRedactorService.generateArticle(title, content);
            log.info("✅ [IA Blog] Artigo gerado com sucesso. Tamanho: {} caracteres",
                    result != null ? result.length() : 0);
            return result;
        } catch (Exception e) {
            log.error("❌ [IA Blog] Erro ao chamar o serviço de redação:", e);
            throw new RuntimeException("Falha ao gerar artigo no Blog: " + e.getMessage(), e);
        }
    }

    public String processYouTubeScript(String title, String content) {
        log.info("🎬 [IA YouTube] Disparando requisição Gemini para Roteiro. Título: '{}'", title);
        try {
            String script = youtubeScriptService.generateScript(title, content);
            log.info("✅ [IA YouTube] Roteiro gerado com sucesso.");
            return script;
        } catch (Exception e) {
            log.error("❌ [IA YouTube] Erro ao gerar roteiro do YouTube:", e);
            throw new RuntimeException("Falha ao gerar roteiro do YouTube: " + e.getMessage(), e);
        }
    }

    public String processLinkedInPost(String title, String content) {
        log.info("💼 [IA LinkedIn] Disparando requisição Gemini para Post. Título: '{}'", title);
        try {
            String post = linkedInContentService.generatedPost(title, content);
            log.info("✅ [IA LinkedIn] Post do LinkedIn gerado com sucesso.");
            return post;
        } catch (Exception e) {
            log.error("❌ [IA LinkedIn] Erro ao gerar post do LinkedIn:", e);
            throw new RuntimeException("Falha ao gerar post do LinkedIn: " + e.getMessage(), e);
        }
    }
}