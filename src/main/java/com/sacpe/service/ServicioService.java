package com.sacpe.service;

import com.sacpe.model.Servicio;
import com.sacpe.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    /**
     * Guarda un nuevo servicio o actualiza uno existente.
     * Valida que no exista otro servicio con el mismo nombre.
     *
     * @param servicio El servicio a guardar.
     * @return El servicio guardado.
     * @throws IllegalStateException si el nombre del servicio ya está en uso.
     */
    @Transactional
    public Servicio guardarServicio(Servicio servicio) throws IllegalStateException {
        Optional<Servicio> servicioExistente = servicioRepository.findByNombreIgnoreCase(servicio.getNombre());

        if (servicioExistente.isPresent() && !servicioExistente.get().getId().equals(servicio.getId())) {
            throw new IllegalStateException("Ya existe un servicio con el nombre '" + servicio.getNombre() + "'.");
        }

        return servicioRepository.save(servicio);
    }

    /**
     * Obtiene todos los servicios del catálogo.
     *
     * @return Una lista de todos los servicios.
     */
    @Transactional(readOnly = true)
    public List<Servicio> buscarTodos() {
        return servicioRepository.findAll();
    }

    /**
     * Busca un servicio por su ID.
     *
     * @param id El ID del servicio a buscar.
     * @return Un Optional que contiene el servicio si se encuentra.
     */
    public Optional<Servicio> buscarPorId(Long id) {
        return servicioRepository.findById(id);
    }

    /**
     * Elimina un servicio por su ID.
     *
     * @param id El ID del servicio a eliminar.
     */
    @Transactional
    public void eliminarServicio(Long id) {
        servicioRepository.deleteById(id);
    }
}