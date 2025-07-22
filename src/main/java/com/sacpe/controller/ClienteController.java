package com.sacpe.controller;

import com.sacpe.model.Cliente;
import com.sacpe.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Muestra la lista de todos los clientes.
     */
    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.buscarTodos());
        return "clientes/listado"; // Devuelve el archivo templates/clientes/listado.html
    }

    /**
     * Muestra el formulario para crear un nuevo cliente.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Nuevo Cliente");
        return "clientes/formulario"; // Devuelve el archivo templates/clientes/formulario.html
    }

    /**
     * Procesa el envío del formulario para guardar un cliente.
     */
    @PostMapping("/guardar")
    public String guardarCliente(@Valid @ModelAttribute("cliente") Cliente cliente,
                                 BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            // Si hay errores de validación, se vuelve a mostrar el formulario con los errores.
            model.addAttribute("titulo", cliente.getId() == null ? "Nuevo Cliente" : "Editar Cliente");
            return "clientes/formulario";
        }

        try {
            clienteService.guardarCliente(cliente);
            attributes.addFlashAttribute("success", "Cliente guardado con éxito.");
        } catch (IllegalStateException e) {
            // Si el servicio lanza un error (ej. email duplicado), se muestra en el formulario.
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/clientes"; // Redirige a la lista de clientes para ver el resultado.
    }

    /**
     * Muestra el formulario para editar un cliente existente.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Cliente inválido:" + id));
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar Cliente");
        return "clientes/formulario";
    }

    /**
     * Elimina un cliente por su ID.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            clienteService.eliminarCliente(id);
            attributes.addFlashAttribute("success", "Cliente eliminado con éxito.");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "No se pudo eliminar el cliente. Es posible que tenga citas asociadas.");
        }
        return "redirect:/clientes";
    }
}