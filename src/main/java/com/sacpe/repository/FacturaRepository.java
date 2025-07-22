package com.sacpe.repository;

import com.sacpe.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    /**
     * Busca una factura por su número único.
     *
     * @param numeroFactura El número de factura a buscar.
     * @return Un Optional que contendrá la factura si se encuentra.
     */
    Optional<Factura> findByNumeroFactura(String numeroFactura);

    /**
     * Busca todas las facturas emitidas dentro de un rango de fechas.
     * Clave para los reportes de ventas por período.
     *
     * @param fechaInicio La fecha y hora de inicio del rango.
     * @param fechaFin La fecha y hora de fin del rango.
     * @return Una lista de facturas emitidas en ese período.
     */
    List<Factura> findByFechaEmisionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Busca todas las facturas asociadas a un cliente específico,
     * ordenadas por la fecha de emisión de forma descendente.
     *
     * @param clienteId El ID del cliente.
     * @return Una lista de las facturas del cliente.
     */
    List<Factura> findByCita_Cliente_IdOrderByFechaEmisionDesc(Long clienteId);

    /**
     * Busca una factura a partir del ID de la cita asociada.
     * Útil para comprobar si una cita ya fue facturada.
     * @param citaId El ID de la cita.
     * @return Un Optional que contiene la factura si existe.
     */
    Optional<Factura> findByCita_Id(Long citaId);

}