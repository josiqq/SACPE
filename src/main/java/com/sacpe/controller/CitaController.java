package com.sacpe.controller;

import com.sacpe.dto.CitaDTO;
import com.sacpe.model.Cita;
import com.sacpe.service.CitaService;
import com.sacpe.service.ClienteService;
import com.sacpe.service.EmpleadoService;
import com.sacpe.service.ServicioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;
    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;
    private final ServicioService servicioService;

    public CitaController(CitaService citaService, ClienteService clienteService, EmpleadoService empleadoService, ServicioService servicioService) {
        this.citaService = citaService;
        this.clienteService = clienteService;
        this.empleadoService = empleadoService;
        this.servicioService = servicioService;
    }

    /**
     * Muestra la página principal de la agenda (el calendario).
     */
    @GetMapping
    public String mostrarAgenda() {
        return "citas/agenda"; // Devuelve templates/citas/agenda.html
    }

    /**
     * Endpoint de API para alimentar el calendario FullCalendar.
     * Devuelve las citas en formato JSON.
     */
    @GetMapping("/api")
    @ResponseBody // Importante: indica que la respuesta es el cuerpo de la petición (JSON)
    public List<CitaDTO> getCitasParaCalendario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        List<Cita> citas = citaService.buscarCitasPorRangoDeFechas(start, end);
        
        return citas.stream().map(cita -> {
            String title = cita.getCliente().getNombre() + " - " + cita.getEstilista().getNombre();
            String color = getColorPorEstado(cita.getEstado().name());
            return new CitaDTO(
                    cita.getId(),
                    title,
                    cita.getFechaHoraInicio().toString(),
                    cita.getFechaHoraFin().toString(),
                    color
            );
        }).collect(Collectors.toList());
    }

    /**
     * Muestra el formulario para crear una nueva cita.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevaCita(Model model) {
        model.addAttribute("cita", new Cita());
        model.addAttribute("clientes", clienteService.buscarTodos());
        model.addAttribute("estilistas", empleadoService.buscarTodosLosEstilistas());
        model.addAttribute("servicios", servicioService.buscarTodos());
        return "citas/formulario"; // Devuelve templates/citas/formulario.html
    }

    /**
     * Procesa el guardado de una nueva cita.
     */
    @PostMapping("/guardar")
    public String guardarCita(@ModelAttribute Cita cita, RedirectAttributes attributes) {
        try {
            // La duración de la cita se calcula sumando la de los servicios
            int duracionTotal = cita.getServicios().stream()
                                  .mapToInt(s -> s.getDuracionEstimadaMinutos())
                                  .sum();
            cita.setFechaHoraFin(cita.getFechaHoraInicio().plusMinutes(duracionTotal));

            citaService.agendarNuevaCita(cita);
            attributes.addFlashAttribute("success", "Cita agendada correctamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al agendar la cita: " + e.getMessage());
        }
        return "redirect:/citas";
    }

    // Método auxiliar para asignar colores a los eventos del calendario
    private String getColorPorEstado(String estado) {
        return switch (estado) {
            case "AGENDADA" -> "#3a87ad"; // Azul
            case "CONFIRMADA" -> "#468847"; // Verde
            case "COMPLETADA" -> "#888888"; // Gris
            case "CANCELADA" -> "#b94a48"; // Rojo
            default -> "#333333";
        };
    }
}