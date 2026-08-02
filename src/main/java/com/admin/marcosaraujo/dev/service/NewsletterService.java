package com.admin.marcosaraujo.dev.service;

import com.admin.marcosaraujo.dev.model.Subscriber;
import com.admin.marcosaraujo.dev.repository.SubscriberRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class NewsletterService {

    private final SubscriberRepository subscriberRepository;

    public NewsletterService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public List<Subscriber> findAll() {
        return subscriberRepository.findAll();
    }

    public Subscriber subscriber(String email) {
        if (subscriberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("E-mail já cadastrado!"); }
        return subscriberRepository.save(new Subscriber(email));
    }

    public long countActive() {
        return subscriberRepository.countByStatus("ACTIVE");

    }
    public void delete(UUID id) {
        subscriberRepository.deleteById(id);
    }

    public long countNewThisMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        return subscriberRepository.countByCreatedAtAfter(startOfMonth);
    }
}
