package com.sacpe.config;

import com.sacpe.service.LicenseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LicenseInterceptor implements HandlerInterceptor {

    private final LicenseService licenseService;

    public LicenseInterceptor(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Rutas públicas que no requieren validación de licencia
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/licencia-expirada") || requestURI.startsWith("/assets/") || requestURI.startsWith("/js/")) {
            return true;
        }

        if (!licenseService.isLicenseValid()) {
            // Si la licencia no es válida, guarda el mensaje y redirige
            request.getSession().setAttribute("licenseError", licenseService.getLicenseMessage());
            response.sendRedirect("/licencia-expirada");
            return false;
        }
        
        return true;
    }
}