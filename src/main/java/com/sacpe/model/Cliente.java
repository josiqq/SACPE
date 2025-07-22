
package com.sacpe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un cliente en la base de datos.
 */
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
    @Column(nullable = false, length = 50)
    private String apellido;

    @Size(max = 20, message = "El teléfono no debe exceder los 20 caracteres.")
    @Column(length = 20)
    private String telefono;

    @Email(message = "Debe ser una dirección de correo electrónico válida.")
    @Size(max = 100, message = "El email no debe exceder los 100 caracteres.")
    @Column(unique = true, length = 100)
    private String email;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String notas;

    // --- Relaciones ---

    // Un cliente puede tener muchas citas.
    // mappedBy: Indica que la entidad Cita es la dueña de la relación (contiene la clave foránea).
    // cascade: Si se elimina un cliente, también se eliminan sus citas.
    // orphanRemoval: Si una cita se quita de esta lista, se elimina de la base de datos.
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Cita> historialVisitas = new ArrayList<>();


    // --- Constructores ---

    public Cliente() {
        // Constructor vacío requerido por JPA
    }

    public Cliente(String nombre, String apellido, String telefono, String email, String notas) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.notas = notas;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public List<Cita> getHistorialVisitas() {
        return historialVisitas;
    }

    public void setHistorialVisitas(List<Cita> historialVisitas) {
        this.historialVisitas = historialVisitas;
    }
}