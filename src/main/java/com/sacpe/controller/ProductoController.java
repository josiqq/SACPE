package com.sacpe.controller;

import com.sacpe.model.Producto;
import com.sacpe.service.ProductoService;
import com.sacpe.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final ProveedorService proveedorService;

    public ProductoController(ProductoService productoService, ProveedorService proveedorService) {
        this.productoService = productoService;
        this.proveedorService = proveedorService;
    }

    /**
     * Muestra la lista de todos los productos en el inventario.
     */
    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.buscarTodos());
        return "productos/listado"; // Devuelve templates/productos/listado.html
    }

    /**
     * Muestra el formulario para registrar un nuevo producto.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("proveedores", proveedorService.buscarTodos()); // Para el <select> de proveedores
        model.addAttribute("titulo", "Nuevo Producto");
        return "productos/formulario"; // Devuelve templates/productos/formulario.html
    }

    /**
     * Procesa el guardado de un producto nuevo o la actualización de uno existente.
     */
    @PostMapping("/guardar")
    public String guardarProducto(@Valid @ModelAttribute("producto") Producto producto,
                                  BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("proveedores", proveedorService.buscarTodos());
            model.addAttribute("titulo", producto.getId() == null ? "Nuevo Producto" : "Editar Producto");
            return "productos/formulario";
        }

        productoService.guardarProducto(producto);
        attributes.addFlashAttribute("success", "Producto guardado con éxito.");
        return "redirect:/productos";
    }

    /**
     * Muestra el formulario para editar un producto existente.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Producto inválido:" + id));
        model.addAttribute("producto", producto);
        model.addAttribute("proveedores", proveedorService.buscarTodos());
        model.addAttribute("titulo", "Editar Producto");
        return "productos/formulario";
    }

    /**
     * Elimina un producto del inventario.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            productoService.eliminarProducto(id);
            attributes.addFlashAttribute("success", "Producto eliminado con éxito.");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al eliminar el producto.");
        }
        return "redirect:/productos";
    }
}