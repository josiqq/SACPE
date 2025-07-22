package com.sacpe.repository;

import com.sacpe.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    /**
     * Busca un servicio por su nombre exacto, ignorando mayúsculas y minúsculas.
     * Útil para validar que no se creen servicios con nombres duplicados.
     *
     * @param nombre El nombre del servicio a buscar.
     * @return Un Optional que contiene el servicio si se encuentra.
     */
    Optional<Servicio> findByNombreIgnoreCase(String nombre);

    /**
     * Busca servicios cuyo nombre contenga el término de búsqueda.
     * Ideal para una función de búsqueda en el catálogo de servicios.
     *
     * @param nombre El término a buscar en el nombre del servicio.
     * @return Una lista de servicios que coinciden.
     */
    List<Servicio> findByNombreContainingIgnoreCase(String nombre);

}