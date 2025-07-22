package com.sacpe.repository;

import com.sacpe.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    /**
     * Busca proveedores cuyo nombre contenga el término de búsqueda.
     * @param nombre El término a buscar.
     * @return Una lista de proveedores que coinciden.
     */
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);

}