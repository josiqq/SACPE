package com.sacpe.service;

import com.sacpe.model.Proveedor;
import com.sacpe.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Guarda un nuevo proveedor o actualiza uno existente.
     *
     * @param proveedor El proveedor a guardar.
     * @return El proveedor guardado.
     */
    @Transactional
    public Proveedor guardarProveedor(Proveedor proveedor) {
        // Aquí se podría añadir validación para evitar duplicados si fuese necesario
        return proveedorRepository.save(proveedor);
    }

    /**
     * Obtiene todos los proveedores.
     *
     * @return Una lista de todos los proveedores.
     */
    @Transactional(readOnly = true)
    public List<Proveedor> buscarTodos() {
        return proveedorRepository.findAll();
    }

    /**
     * Busca un proveedor por su ID.
     *
     * @param id El ID del proveedor.
     * @return Un Optional que contiene al proveedor si se encuentra.
     */
    public Optional<Proveedor> buscarPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    /**
     * Elimina un proveedor por su ID.
     *
     * @param id El ID del proveedor a eliminar.
     */
    @Transactional
    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }
}