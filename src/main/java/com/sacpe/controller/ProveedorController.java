package com.sacpe.controller;

import com.sacpe.model.Proveedor;
import com.sacpe.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    /**
     * Muestra la lista de todos los proveedores.
     */
    @GetMapping
    public String listarProveedores(Model model) {
        model.addAttribute("proveedores", proveedorService.buscarTodos());
        return "proveedores/listado"; // Devuelve templates/proveedores/listado.html
    }

    /**
     * Muestra el formulario para registrar un nuevo proveedor.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("titulo", "Nuevo Proveedor");
        return "proveedores/formulario"; // Devuelve templates/proveedores/formulario.html
    }

    /**
     * Procesa el guardado de un proveedor nuevo o la actualización de uno existente.
     */
    @PostMapping("/guardar")
    public String guardarProveedor(@Valid @ModelAttribute("proveedor") Proveedor proveedor,
                                   BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", proveedor.getId() == null ? "Nuevo Proveedor" : "Editar Proveedor");
            return "proveedores/formulario";
        }

        try {
            proveedorService.guardarProveedor(proveedor);
            attributes.addFlashAttribute("success", "Proveedor guardado con éxito.");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al guardar el proveedor.");
        }

        return "redirect:/proveedores";
    }

    /**
     * Muestra el formulario para editar un proveedor existente.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Proveedor proveedor = proveedorService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Proveedor inválido:" + id));
        model.addAttribute("proveedor", proveedor);
        model.addAttribute("titulo", "Editar Proveedor");
        return "proveedores/formulario";
    }

    /**
     * Elimina un proveedor del sistema.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            proveedorService.eliminarProveedor(id);
            attributes.addFlashAttribute("success", "Proveedor eliminado con éxito.");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "No se pudo eliminar el proveedor. Verifique que no esté asociado a ningún producto.");
        }
        return "redirect:/proveedores";
    }
}