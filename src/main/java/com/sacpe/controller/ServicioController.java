package com.sacpe.controller;

import com.sacpe.model.Servicio;
import com.sacpe.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    /**
     * Muestra la lista de todos los servicios.
     */
    @GetMapping
    public String listarServicios(Model model) {
        model.addAttribute("servicios", servicioService.buscarTodos());
        return "servicios/listado"; // Devuelve templates/servicios/listado.html
    }

    /**
     * Muestra el formulario para registrar un nuevo servicio.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("servicio", new Servicio());
        model.addAttribute("titulo", "Nuevo Servicio");
        return "servicios/formulario"; // Devuelve templates/servicios/formulario.html
    }

    /**
     * Procesa el guardado de un servicio nuevo o la actualización de uno existente.
     */
    @PostMapping("/guardar")
    public String guardarServicio(@Valid @ModelAttribute("servicio") Servicio servicio,
                                  BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", servicio.getId() == null ? "Nuevo Servicio" : "Editar Servicio");
            return "servicios/formulario";
        }

        try {
            servicioService.guardarServicio(servicio);
            attributes.addFlashAttribute("success", "Servicio guardado con éxito.");
        } catch (IllegalStateException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/servicios";
    }

    /**
     * Muestra el formulario para editar un servicio existente.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Servicio servicio = servicioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Servicio inválido:" + id));
        model.addAttribute("servicio", servicio);
        model.addAttribute("titulo", "Editar Servicio");
        return "servicios/formulario";
    }

    /**
     * Elimina un servicio del sistema.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            servicioService.eliminarServicio(id);
            attributes.addFlashAttribute("success", "Servicio eliminado con éxito.");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "No se pudo eliminar el servicio. Verifique que no esté asociado a ninguna cita.");
        }
        return "redirect:/servicios";
    }
}