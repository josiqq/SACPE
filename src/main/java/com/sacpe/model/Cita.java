package com.sacpe.model;

import com.sacpe.enums.EstadoCita;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa una cita o turno en la peluquería.
 * Conecta Clientes, Empleados y Servicios.
 */
@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha y hora de inicio son obligatorias.")
    @Future(message = "La cita debe ser programada para una fecha futura.")
    @Column(nullable = false)
    private LocalDateTime fechaHoraInicio;

    @NotNull(message = "La fecha y hora de fin son obligatorias.")
    @Column(nullable = false)
    private LocalDateTime fechaHoraFin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCita estado;

    // --- Relaciones ---

    @NotNull(message = "La cita debe estar asociada a un cliente.")
    @ManyToOne(fetch = FetchType.LAZY) // Muchas citas pueden pertenecer a un cliente.
    @JoinColumn(name = "cliente_id", nullable = false) // Define la columna de la clave foránea.
    private Cliente cliente;

    @NotNull(message = "La cita debe estar asignada a un estilista.")
    @ManyToOne(fetch = FetchType.LAZY) // Muchas citas pueden ser atendidas por un empleado.
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado estilista;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cita_servicios", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "cita_id"),
            inverseJoinColumns = @JoinColumn(name = "servicio_id")
    )
    private Set<Servicio> servicios = new HashSet<>();


    // --- Constructores ---

    public Cita() {
        // Constructor vacío requerido por JPA
    }


    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getEstilista() {
        return estilista;
    }

    public void setEstilista(Empleado estilista) {
        this.estilista = estilista;
    }

    public Set<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(Set<Servicio> servicios) {
        this.servicios = servicios;
    }
}