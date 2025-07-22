package com.sacpe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /**
     * Muestra la página de login personalizada.
     */
    @GetMapping("/login")
    public String login() {
        return "login"; // Devuelve templates/login.html
    }

    /**
     * Redirige la ruta raíz ("/") a la página de la agenda.
     * Si el usuario no ha iniciado sesión, Spring Security lo interceptará
     * y lo redirigirá automáticamente a la página de /login.
     */
    @GetMapping("/")
    public String redirectToAgenda() {
        return "redirect:/citas";
    }
}