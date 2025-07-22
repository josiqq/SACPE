package com.sacpe.service;

import com.sacpe.enums.EstadoCita;
import com.sacpe.enums.EstadoFactura;
import com.sacpe.enums.MetodoPago;
import com.sacpe.model.Cita;
import com.sacpe.model.Factura;
import com.sacpe.model.Servicio;
import com.sacpe.repository.CitaRepository;
import com.sacpe.repository.FacturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final CitaRepository citaRepository;

    public FacturaService(FacturaRepository facturaRepository, CitaRepository citaRepository) {
        this.facturaRepository = facturaRepository;
        this.citaRepository = citaRepository;
    }

    /**
     * Genera una factura para una cita que ha sido completada.
     *
     * @param citaId El ID de la cita a facturar.
     * @param metodoPago El método de pago utilizado.
     * @return La factura recién creada.
     * @throws IllegalStateException si la cita no existe, no está completada o ya fue facturada.
     */
    @Transactional
    public Factura generarFacturaDeCita(Long citaId, MetodoPago metodoPago) throws IllegalStateException {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new IllegalStateException("No se encontró la cita con ID: " + citaId));

        // --- MEJORA 1: Validar estado ---
        if (cita.getEstado() != EstadoCita.COMPLETADA) {
            throw new IllegalStateException("Solo se pueden facturar citas en estado 'COMPLETADA'.");
        }

        // --- MEJORA 2: Validar que no exista ya una factura para esta cita ---
        if (facturaRepository.findByCita_Id(citaId).isPresent()) {
            throw new IllegalStateException("La cita con ID: " + citaId + " ya ha sido facturada.");
        }

        // ... resto del método sin cambios ...
        BigDecimal montoTotal = cita.getServicios().stream()
                .map(Servicio::getPrecio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Factura factura = new Factura();
        factura.setCita(cita);
        factura.setFechaEmision(LocalDateTime.now());
        factura.setMontoTotal(montoTotal);
        factura.setMetodoPago(metodoPago);
        factura.setEstado(EstadoFactura.PAGADA);
        factura.setNumeroFactura(generarNumeroFactura());

        return facturaRepository.save(factura);
    }

    /**
     * Anula una factura existente.
     *
     * @param facturaId El ID de la factura a anular.
     * @return La factura actualizada con el estado ANULADA.
     */
    @Transactional
    public Factura anularFactura(Long facturaId) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new IllegalStateException("No se encontró la factura con ID: " + facturaId));

        factura.setEstado(EstadoFactura.ANULADA);
        return facturaRepository.save(factura);
    }

    /**
     * Busca facturas en un rango de fechas.
     *
     * @param inicio Fecha de inicio del rango.
     * @param fin Fecha de fin del rango.
     * @return Lista de facturas.
     */
    @Transactional(readOnly = true)
    public List<Factura> buscarFacturasPorRango(LocalDateTime inicio, LocalDateTime fin) {
        return facturaRepository.findByFechaEmisionBetween(inicio, fin);
    }

    /**
     * Busca una factura por su ID.
     * @param id El ID de la factura a buscar.
     * @return Un Optional que contiene la factura si se encuentra.
     */
    public Optional<Factura> buscarPorId(Long id) {
        return facturaRepository.findById(id);
    }

    /**
     * Genera un número de factura único.
     * (Esta es una implementación simple, se puede mejorar con secuencias de BD)
     *
     * @return Un string con el número de factura.
     */
    private String generarNumeroFactura() {
        return "FACT-" + System.currentTimeMillis();
    }
}