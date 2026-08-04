package com.admin.marcosaraujo.dev.repository;

import com.admin.marcosaraujo.dev.entity.CuratedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuratedItemRepository extends JpaRepository<CuratedItem, Long> {


    List<CuratedItem> findByCategoryOrderByCreatedAtDesc(String category);

    List<CuratedItem> findByCategoryIsNull();

    List<CuratedItem> findByBlogStatus(String blogStatus);

    List<CuratedItem> findByYoutubeStatus(String youtubeStatus);

    List<CuratedItem> findByLinkedinStatus(String linkedinStatus);
}