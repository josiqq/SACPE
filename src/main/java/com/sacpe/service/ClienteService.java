package com.sacpe.service;

import com.sacpe.model.Cliente;
import com.sacpe.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Guarda un cliente nuevo o actualiza uno existente.
     * Valida que el email no esté en uso por otro cliente.
     *
     * @param cliente El cliente a guardar.
     * @return El cliente guardado.
     * @throws IllegalStateException si el email ya está registrado por otro cliente.
     */
    @Transactional
    public Cliente guardarCliente(Cliente cliente) throws IllegalStateException {
        // Si el cliente tiene un email, se verifica que no exista en otro cliente
        if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
            Optional<Cliente> clienteExistente = clienteRepository.findByEmail(cliente.getEmail());
            
            // Si el email existe y pertenece a un cliente con un ID diferente, lanza un error.
            if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(cliente.getId())) {
                throw new IllegalStateException("El email '" + cliente.getEmail() + "' ya está registrado.");
            }
        }
        return clienteRepository.save(cliente);
    }

    /**
     * Obtiene todos los clientes de la base de datos.
     *
     * @return Una lista de todos los clientes.
     */
    @Transactional(readOnly = true)
    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    /**
     * Busca un cliente por su ID.
     *
     * @param id El ID del cliente a buscar.
     * @return Un Optional que contiene al cliente si se encuentra.
     */
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    /**
     * Elimina un cliente por su ID.
     *
     * @param id El ID del cliente a eliminar.
     */
    @Transactional
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    /**
     * Busca clientes por nombre o apellido.
     *
     * @param termino El texto a buscar en el nombre o apellido.
     * @return Una lista de clientes que coinciden.
     */
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombreOApellido(String termino) {
        return clienteRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(termino, termino);
    }
}