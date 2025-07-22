package com.sacpe.controller;

import com.sacpe.enums.MetodoPago;
import com.sacpe.model.Factura;
import com.sacpe.service.CitaService;
import com.sacpe.service.FacturaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    private final FacturaService facturaService;
    private final CitaService citaService;

    public FacturaController(FacturaService facturaService, CitaService citaService) {
        this.facturaService = facturaService;
        this.citaService = citaService;
    }

    /**
     * Muestra la lista de citas completadas que están listas para ser facturadas.
     */
    @GetMapping("/pendientes")
    public String listarCitasParaFacturar(Model model) {
        // --- MEJORA 1: Usar el método correcto del servicio ---
        model.addAttribute("citas", citaService.buscarCitasPendientesDeFacturacion());
        return "facturas/pendientes";
    }

    /**
     * Procesa la generación de una nueva factura a partir de una cita.
     * @param citaId El ID de la cita a facturar.
     * @param metodoPago El método de pago seleccionado por el usuario.
     */
    @PostMapping("/generar")
    public String generarFactura(@RequestParam("citaId") Long citaId,
                                 @RequestParam("metodoPago") MetodoPago metodoPago,
                                 RedirectAttributes attributes) {
        try {
            facturaService.generarFacturaDeCita(citaId, metodoPago);
            attributes.addFlashAttribute("success", "Factura generada con éxito.");
        } catch (IllegalStateException e) {
            attributes.addFlashAttribute("error", "No se pudo generar la factura: " + e.getMessage());
            return "redirect:/facturas/pendientes";
        }
        return "redirect:/citas"; // Redirige a la agenda o a una lista de facturas
    }

    /**
     * Muestra los detalles de una factura específica.
     * 
     */
     @GetMapping("/{id}")
    public String verFactura(@PathVariable Long id, Model model) {
        // --- MEJORA 2: Usar el método buscarPorId ---
        Factura factura = facturaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Factura inválido: " + id));
        model.addAttribute("factura", factura);
        return "facturas/detalle";
    }
}