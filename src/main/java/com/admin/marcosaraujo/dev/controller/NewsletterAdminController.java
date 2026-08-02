package com.admin.marcosaraujo.dev.controller;

import com.admin.marcosaraujo.dev.service.NewsletterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@Controller
@RequestMapping("/newsletter")
public class NewsletterAdminController {

    private final NewsletterService newsletterService;

    public NewsletterAdminController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("subscribers", newsletterService.findAll());
        model.addAttribute("activeSubscribersCount", newsletterService.countActive());
        model.addAttribute("newThisMonthCount", newsletterService.countNewThisMonth());
        return "newsletter";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        newsletterService.delete(id);
        return "redirect:/newsletter";
    }
}

