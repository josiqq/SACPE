package com.sacpe.controller;

import com.sacpe.model.Empleado;
import com.sacpe.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    /**
     * Muestra la lista de todos los empleados.
     */
    @GetMapping
    public String listarEmpleados(Model model) {
        model.addAttribute("empleados", empleadoService.buscarTodos());
        return "empleados/listado"; // Devuelve templates/empleados/listado.html
    }

    /**
     * Muestra el formulario para registrar un nuevo empleado.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("empleado", new Empleado());
        model.addAttribute("titulo", "Nuevo Empleado");
        return "empleados/formulario"; // Devuelve templates/empleados/formulario.html
    }

    /**
     * Procesa el guardado de un empleado nuevo o la actualización de uno existente.
     */
    @PostMapping("/guardar")
    public String guardarEmpleado(@Valid @ModelAttribute("empleado") Empleado empleado,
                                  BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", empleado.getId() == null ? "Nuevo Empleado" : "Editar Empleado");
            return "empleados/formulario";
        }

        try {
            empleadoService.guardarEmpleado(empleado);
            attributes.addFlashAttribute("success", "Empleado guardado con éxito.");
        } catch (IllegalStateException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/empleados/nuevo";
        }

        return "redirect:/empleados";
    }

    /**
     * Muestra el formulario para editar un empleado existente.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Empleado empleado = empleadoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Empleado inválido:" + id));
        model.addAttribute("empleado", empleado);
        model.addAttribute("titulo", "Editar Empleado");
        return "empleados/formulario";
    }

    /**
     * Elimina un empleado del sistema.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            empleadoService.eliminarEmpleado(id);
            attributes.addFlashAttribute("success", "Empleado eliminado con éxito.");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "No se pudo eliminar al empleado. Verifique que no tenga citas asociadas.");
        }
        return "redirect:/empleados";
    }
}