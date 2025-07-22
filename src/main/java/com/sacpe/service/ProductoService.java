package com.sacpe.service;

import com.sacpe.model.Producto;
import com.sacpe.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Guarda un nuevo producto o actualiza uno existente.
     *
     * @param producto El producto a guardar.
     * @return El producto guardado.
     */
    @Transactional
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Obtiene todos los productos del inventario.
     *
     * @return Una lista de todos los productos.
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarTodos() {
        return productoRepository.findAll();
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id El ID del producto.
     * @return Un Optional que contiene el producto si se encuentra.
     */
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * Ajusta el stock de un producto.
     *
     * @param productoId El ID del producto a actualizar.
     * @param cantidad   La cantidad a añadir (si es positiva) o a restar (si es negativa).
     * @return El producto con el stock actualizado.
     * @throws IllegalStateException si el producto no existe o si se intenta dejar el stock en negativo.
     */
    @Transactional
    public Producto actualizarStock(Long productoId, int cantidad) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalStateException("Producto no encontrado con ID: " + productoId));

        int nuevoStock = producto.getStock() + cantidad;
        if (nuevoStock < 0) {
            throw new IllegalStateException("No se puede tener stock negativo. Stock actual: " + producto.getStock());
        }

        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }

    /**
     * Devuelve una lista de productos que están en su punto de reorden o por debajo.
     *
     * @return Lista de productos con bajo stock.
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarProductosConBajoStock() {
        return productoRepository.findProductosConBajoStock();
    }
    
    /**
     * Elimina un producto por su ID.
     *
     * @param id El ID del producto a eliminar.
     */
    @Transactional
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}