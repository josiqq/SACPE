package com.sacpe.repository;

import com.sacpe.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por su dirección de correo electrónico.
     * Como el email es único, devuelve un Optional.
     *
     * @param email El email a buscar.
     * @return Un Optional que contiene al cliente si se encuentra.
     */
    Optional<Cliente> findByEmail(String email);

    /**
     * Busca clientes cuyo nombre o apellido contenga el término de búsqueda.
     * Útil para una barra de búsqueda de clientes.
     *
     * @param nombre Término de búsqueda para el nombre.
     * @param apellido Término de búsqueda para el apellido.
     * @return Una lista de clientes que coinciden con la búsqueda.
     */
    List<Cliente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);

}