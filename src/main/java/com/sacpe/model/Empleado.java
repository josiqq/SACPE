package com.sacpe.model;

import com.sacpe.enums.PuestoEmpleado;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un empleado de la peluquería.
 * Este modelo se puede extender para vincularlo a un usuario del sistema para el login.
 */
@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío.")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String apellido;

    @NotNull(message = "El puesto es obligatorio.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PuestoEmpleado puesto;
    
    @Email(message = "Debe ser una dirección de correo electrónico válida.")
    @Size(max = 100)
    @Column(unique = true, length = 100)
    private String email;

    @Size(max = 20)
    @Column(length = 20)
    private String telefono;

    @NotEmpty(message = "La contraseña no puede estar vacía.")
    @Column(nullable = false)
    private String password;

    @DecimalMin(value = "0.0", message = "La comisión no puede ser negativa.")
    @Column(precision = 5, scale = 2) // Ej: 5.25% o 15.50%
    private BigDecimal comision;

    // --- Relaciones ---

    // Un empleado puede atender muchas citas.
    // El 'mappedBy' apunta al campo 'estilista' en la entidad Cita.
    // No se usa CascadeType.REMOVE para no borrar citas históricas si se elimina un empleado.
    @OneToMany(
            mappedBy = "estilista",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Cita> citasAtendidas = new ArrayList<>();


    // --- Constructores ---

    public Empleado() {
        // Constructor vacío requerido por JPA
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public PuestoEmpleado getPuesto() {
        return puesto;
    }

    public void setPuesto(PuestoEmpleado puesto) {
        this.puesto = puesto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Cita> getCitasAtendidas() {
        return citasAtendidas;
    }

    public void setCitasAtendidas(List<Cita> citasAtendidas) {
        this.citasAtendidas = citasAtendidas;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getComision() {
        return comision;
    }

    public void setComision(BigDecimal comision) {
        this.comision = comision;
    }
}