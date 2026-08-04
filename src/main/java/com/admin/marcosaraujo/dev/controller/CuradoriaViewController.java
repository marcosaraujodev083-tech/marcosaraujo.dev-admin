package com.admin.marcosaraujo.dev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CuradoriaViewController {

    @GetMapping("/curadoria")
    public String renderCuradoriaPage() {
        // Retorna o template 'src/main/resources/templates/curadoria.html'
        return "curadoria";
    }
}