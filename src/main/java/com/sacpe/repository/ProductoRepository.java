package com.sacpe.repository;

import com.sacpe.enums.CategoriaProducto;
import com.sacpe.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca productos cuyo nombre contenga el término de búsqueda, ignorando mayúsculas/minúsculas.
     * Perfecto para una funcionalidad de búsqueda de productos.
     *
     * @param nombre El término a buscar en el nombre del producto.
     * @return Una lista de productos que coinciden.
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca todos los productos que están por debajo o en su punto de reorden.
     * Esencial para las alertas de bajo stock.
     *
     * @return Una lista de productos que necesitan ser reabastecidos.
     */
    @Query("SELECT p FROM Producto p WHERE p.stock <= p.puntoDeReorden")
    List<Producto> findProductosConBajoStock();

    /**
     * Busca todos los productos que pertenecen a una categoría específica.
     *
     * @param categoria La categoría de producto a buscar.
     * @return Una lista de productos de esa categoría.
     */
    List<Producto> findByCategoria(CategoriaProducto categoria);

}