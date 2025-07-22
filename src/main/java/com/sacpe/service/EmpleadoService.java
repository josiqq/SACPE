package com.sacpe.service;

import com.sacpe.enums.PuestoEmpleado;
import com.sacpe.model.Empleado;
import com.sacpe.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    /**
     * Guarda un nuevo empleado o actualiza uno existente.
     * Valida que el email no esté en uso por otro empleado.
     *
     * @param empleado El empleado a guardar.
     * @return El empleado guardado.
     */
    @Transactional
    public Empleado guardarEmpleado(Empleado empleado) {
        if (empleado.getEmail() != null && !empleado.getEmail().isEmpty()) {
            Optional<Empleado> empleadoExistente = empleadoRepository.findByEmail(empleado.getEmail());
            if (empleadoExistente.isPresent() && !empleadoExistente.get().getId().equals(empleado.getId())) {
                throw new IllegalStateException("El email '" + empleado.getEmail() + "' ya está en uso.");
            }
        }
        return empleadoRepository.save(empleado);
    }

    /**
     * Devuelve una lista de todos los empleados.
     *
     * @return Lista de todos los empleados.
     */
    @Transactional(readOnly = true)
    public List<Empleado> buscarTodos() {
        return empleadoRepository.findAll();
    }

    /**
     * Devuelve una lista de todos los empleados que son estilistas.
     * Esencial para la pantalla de agendamiento de citas.
     *
     * @return Lista de empleados con el puesto de ESTILISTA.
     */
    @Transactional(readOnly = true)
    public List<Empleado> buscarTodosLosEstilistas() {
        return empleadoRepository.findByPuesto(PuestoEmpleado.ESTILISTA);
    }

    /**
     * Busca un empleado por su ID.
     *
     * @param id El ID del empleado.
     * @return Un Optional que contiene al empleado si se encuentra.
     */
    public Optional<Empleado> buscarPorId(Long id) {
        return empleadoRepository.findById(id);
    }

    /**
     * Elimina un empleado por su ID.
     *
     * @param id El ID del empleado a eliminar.
     */
    @Transactional
    public void eliminarEmpleado(Long id) {
        empleadoRepository.deleteById(id);
    }
}