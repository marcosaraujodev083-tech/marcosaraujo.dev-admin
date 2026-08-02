package com.admin.marcosaraujo.dev.controller;

import com.admin.marcosaraujo.dev.repository.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final PostRepository postRepository;

    public HomeController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "index";
    }
}
