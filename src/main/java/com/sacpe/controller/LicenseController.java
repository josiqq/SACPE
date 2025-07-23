package com.sacpe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LicenseController {

    @GetMapping("/licencia-expirada")
    public String mostrarPaginaLicencia() {
        return "licencia_expirada";
    }
}