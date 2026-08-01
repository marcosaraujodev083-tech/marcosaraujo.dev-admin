package com.admin.marcosaraujo.dev.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean draft = true; // Por padrão, nasce como Rascunho

    private Integer readingTimeMinutes;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime publishedAt; // Data/Hora que o artigo deve ir ao ar

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    public Post() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();

        this.calculateReadingTime();

        if (!this.draft && this.publishedAt == null) {
            this.publishedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();

        this.calculateReadingTime();

        if (!this.draft && this.publishedAt == null) {
            this.publishedAt = LocalDateTime.now();
        }
    }

    // Método privado auxiliar para estimar o tempo de leitura (200 palavras/minuto)
    private void calculateReadingTime() {
        if (this.content == null || this.content.isBlank()) {
            this.readingTimeMinutes = 1;
            return;
        }

        String[] words = this.content.trim().split("\\s+");
        int minutes = (int) Math.ceil((double) words.length / 200.0);
        this.readingTimeMinutes = Math.max(1, minutes);
    }

    public boolean isScheduled() {
        return !this.draft && this.publishedAt != null && this.publishedAt.isAfter(LocalDateTime.now());
    }

    public boolean isPublished() {
        return !this.draft && this.publishedAt != null && !this.publishedAt.isAfter(LocalDateTime.now());
    }

    // --- GETTERS E SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    // TRATAMENTO DE NULL: Se o campo no banco for null (posts antigos), devolve 1 min
    public Integer getReadingTimeMinutes() {
        return readingTimeMinutes != null ? readingTimeMinutes : 1;
    }

    public void setReadingTimeMinutes(Integer readingTimeMinutes) {
        this.readingTimeMinutes = readingTimeMinutes;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}