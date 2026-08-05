package com.admin.marcosaraujo.dev.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "curated_items")
public class CuratedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "subtitle", columnDefinition = "TEXT")
    private String subtitle;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(name = "source_name")
    private String sourceName;

    private String author;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    @Column(name = "description", columnDefinition = "TEXT")
    private String content;

    @Column(name = "raw_content", columnDefinition = "TEXT")
    private String rawContent;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    private String category;

    private String status;

    @Column(name = "blog_content", columnDefinition = "TEXT")
    private String blogContent;

    // ⚡ Mapeia a coluna PostgreSQL do tipo JSONB para String no Java
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "youtube_script", columnDefinition = "jsonb")
    private String youtubeScript;

    @Column(name = "linkedin_content", columnDefinition = "TEXT")
    private String linkedinContent;

    @Column(name = "blog_status")
    private String blogStatus;

    @Column(name = "youtube_status")
    private String youtubeStatus;

    @Column(name = "linkedin_status")
    private String linkedinStatus;

    public CuratedItem() {
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public OffsetDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(OffsetDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRawContent() {
        return rawContent;
    }

    public void setRawContent(String rawContent) {
        this.rawContent = rawContent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public String getYoutubeScript() {
        return youtubeScript;
    }

    public void setYoutubeScript(String youtubeScript) {
        this.youtubeScript = youtubeScript;
    }

    public String getLinkedinContent() {
        return linkedinContent;
    }

    public void setLinkedinContent(String linkedinContent) {
        this.linkedinContent = linkedinContent;
    }

    public String getBlogStatus() {
        return blogStatus;
    }

    public void setBlogStatus(String blogStatus) {
        this.blogStatus = blogStatus;
    }

    public String getYoutubeStatus() {
        return youtubeStatus;
    }

    public void setYoutubeStatus(String youtubeStatus) {
        this.youtubeStatus = youtubeStatus;
    }

    public String getLinkedinStatus() {
        return linkedinStatus;
    }

    public void setLinkedinStatus(String linkedinStatus) {
        this.linkedinStatus = linkedinStatus;
    }
}