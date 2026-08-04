package com.admin.marcosaraujo.dev.controller;

import com.admin.marcosaraujo.dev.entity.CuratedItem;
import com.admin.marcosaraujo.dev.model.Post;
import com.admin.marcosaraujo.dev.repository.CuratedItemRepository;
import com.admin.marcosaraujo.dev.repository.PostRepository;
import com.admin.marcosaraujo.dev.service.automation.CuratedItemAutomationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/admin/curadoria")
@CrossOrigin(origins = "*")
public class CuradoriaController {

    private final CuratedItemAutomationService automationService;
    private final CuratedItemRepository repository;
    private final PostRepository postRepository; // ⚡ Injetando o repositório de Post

    public CuradoriaController(
            CuratedItemAutomationService automationService,
            CuratedItemRepository repository,
            PostRepository postRepository) {
        this.automationService = automationService;
        this.repository = repository;
        this.postRepository = postRepository;
    }

    // LISTAGEM E FILTROS (Triagem por Abas)

    @GetMapping("/categoria/{category}")
    public ResponseEntity<List<CuratedItem>> getByCategory(@PathVariable String category) {
        List<CuratedItem> items;
        if ("TODAS".equalsIgnoreCase(category) || "Geral".equalsIgnoreCase(category)) {
            items = repository.findAll();
        } else {
            items = repository.findByCategoryOrderByCreatedAtDesc(category);
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<CuratedItem>> getPending() {
        List<CuratedItem> items = repository.findByCategoryIsNull();
        return ResponseEntity.ok(items);
    }

    // ESTEIRAS DE IA

    @PostMapping("/{id}/categorize")
    public ResponseEntity<CuratedItem> categorizeArticle(@PathVariable Long id) {
        CuratedItem item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matéria não encontrada com ID: " + id));

        String category = automationService.processCategorization(item.getTitle(), item.getContent());
        item.setCategory(category);

        CuratedItem updatedItem = repository.save(item);
        return ResponseEntity.ok(updatedItem);
    }

    @PostMapping("/{id}/generate-blog")
    public ResponseEntity<CuratedItem> generateBlogDraft(@PathVariable Long id) {
        CuratedItem item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matéria não encontrada com ID: " + id));

        String blogContent = automationService.processBlogDraft(item.getTitle(), item.getContent());

        item.setBlogContent(blogContent);
        item.setBlogStatus("rascunho_pronto");

        CuratedItem updatedItem = repository.save(item);
        return ResponseEntity.ok(updatedItem);
    }

    // ⚡ APROVAR E ENVIAR PARA A TABELA DE POSTS (RASCUNHO DO BLOG)
    @PostMapping("/{id}/approve-blog")
    public ResponseEntity<CuratedItem> approveBlogDraft(@PathVariable Long id) {
        CuratedItem item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matéria não encontrada com ID: " + id));

        if (item.getBlogContent() == null || item.getBlogContent().isBlank()) {
            throw new RuntimeException("Não há conteúdo de blog gerado para aprovação.");
        }

        // 1. Atualiza o status do item de curadoria
        item.setBlogStatus("aprovado");
        item.setStatus("PROCESSADO");
        CuratedItem updatedItem = repository.save(item);

        // 2. Instancia e salva o novo Rascunho de Artigo na tabela 'posts'
        Post newPost = new Post();
        newPost.setTitle(item.getTitle());
        newPost.setSlug(generateSlug(item.getTitle()));
        newPost.setSummary(item.getContent() != null ? item.getContent() : item.getTitle());
        newPost.setContent(item.getBlogContent());
        newPost.setDraft(true); // Garante que entra como Rascunho

        postRepository.save(newPost); // Salva na tabela 'posts'

        return ResponseEntity.ok(updatedItem);
    }

    @PostMapping("/{id}/generate-youtube")
    public ResponseEntity<CuratedItem> generateYouTubeScript(@PathVariable Long id) {
        CuratedItem item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matéria não encontrada com ID: " + id));

        String scriptJson = automationService.processYouTubeScript(item.getTitle(), item.getContent());

        item.setYoutubeScript(scriptJson);
        item.setYoutubeStatus("em_producao");

        CuratedItem updatedItem = repository.save(item);
        return ResponseEntity.ok(updatedItem);
    }

    @PostMapping("/{id}/generate-linkedin")
    public ResponseEntity<CuratedItem> generateLinkedInPost(@PathVariable Long id) {
        CuratedItem item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matéria não encontrada com ID: " + id));

        String linkedinContent = automationService.processLinkedInPost(item.getTitle(), item.getContent());

        item.setLinkedinContent(linkedinContent);
        item.setLinkedinStatus("pronto_para_postar");

        CuratedItem updatedItem = repository.save(item);
        return ResponseEntity.ok(updatedItem);
    }

    // 🛠️ Função utilitária para gerar slug amigável (ex: "Novo Artigo!" -> "novo-artigo")
    private String generateSlug(String input) {
        if (input == null) return "sem-titulo-" + System.currentTimeMillis();

        String nowhitespace = Pattern.compile("\\s+").matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = Pattern.compile("[^\\w-]").matcher(normalized).replaceAll("");

        return slug.toLowerCase(Locale.ENGLISH) + "-" + System.currentTimeMillis();
    }
}