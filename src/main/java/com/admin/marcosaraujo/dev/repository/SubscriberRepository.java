package com.admin.marcosaraujo.dev.repository;

import com.admin.marcosaraujo.dev.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {

    long countByStatus(String status);

    long countByCreatedAtAfter(LocalDateTime date);

    boolean existsByEmail(String email);
}