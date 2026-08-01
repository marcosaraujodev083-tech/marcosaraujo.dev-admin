package com.admin.marcosaraujo.dev.controller;

import com.admin.marcosaraujo.dev.model.Post;
import com.admin.marcosaraujo.dev.repository.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Formulário de Criação (Inicializa data para o formulário Thymeleaf/HTML)
    @GetMapping("/new")
    public String newPostForm(Model model) {
        Post post = new Post();
        post.setPublishedAt(LocalDateTime.now());
        model.addAttribute("post", post);
        return "posts/form";
    }

    // Formulário de Edição
    @GetMapping("/edit/{id}")
    public String editPostForm(@PathVariable("id") Long id, Model model) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post inválido ID: " + id));

        if (post.getPublishedAt() == null) {
            post.setPublishedAt(LocalDateTime.now());
        }

        model.addAttribute("post", post);
        return "posts/form";
    }

    // Salvar / Atualizar Post
    @PostMapping
    public String savePost(@ModelAttribute("post") Post post) {
        String generatedSlug;
        if (post.getSlug() != null && !post.getSlug().isBlank()) {
            generatedSlug = generateSlug(post.getSlug());
        } else {
            generatedSlug = generateSlug(post.getTitle());
        }

        if (generatedSlug.isBlank()) {
            generatedSlug = "post-" + System.currentTimeMillis();
        }

        // Lógica para garantir Slug Único
        String baseSlug = generatedSlug;
        int count = 1;
        while (true) {
            Optional<Post> existingPost = postRepository.findBySlug(generatedSlug);

            // Se o slug não existe no banco, OU se pertence ao próprio post que estamos editando: OK!
            if (existingPost.isEmpty() || (post.getId() != null && existingPost.get().getId().equals(post.getId()))) {
                break;
            }

            generatedSlug = baseSlug + "-" + count;
            count++;
        }

        post.setSlug(generatedSlug);

        // Se o usuário desmarcar rascunho e não tiver escolhido data, garante a data atual
        if (!post.isDraft() && post.getPublishedAt() == null) {
            post.setPublishedAt(LocalDateTime.now());
        }

        postRepository.save(post);
        return "redirect:/";
    }

    // Excluir Post
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        postRepository.deleteById(id);
        return "redirect:/";
    }

    // Gerador de Slug Amigável (Remove acentos, caracteres especiais e converte espaços em hífen)
    private String generateSlug(String input) {
        if (input == null || input.isBlank()) return "";
        String normalized = Normalizer.normalize(input.trim(), Normalizer.Form.NFD);
        String withoutAccents = normalized.replaceAll("\\p{M}", "");
        String slug = withoutAccents
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
        return slug.replaceAll("^-|-$", "");
    }
}