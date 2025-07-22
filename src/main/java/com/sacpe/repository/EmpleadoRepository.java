package com.sacpe.repository;

import com.sacpe.enums.PuestoEmpleado;
import com.sacpe.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    /**
     * Busca un empleado por su dirección de correo electrónico.
     * Esencial para la funcionalidad de login.
     *
     * @param email El email del empleado a buscar.
     * @return Un Optional que contiene al empleado si se encuentra.
     */
    Optional<Empleado> findByEmail(String email);

    /**
     * Busca todos los empleados que tienen un puesto específico.
     * Muy útil para obtener la lista de todos los estilistas disponibles.
     *
     * @param puesto El puesto de empleado a buscar (ej. PuestoEmpleado.ESTILISTA).
     * @return Una lista de empleados con ese puesto.
     */
    List<Empleado> findByPuesto(PuestoEmpleado puesto);

}