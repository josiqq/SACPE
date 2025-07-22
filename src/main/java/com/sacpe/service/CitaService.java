package com.sacpe.service;

import com.sacpe.enums.EstadoCita;
import com.sacpe.model.Cita;
import com.sacpe.repository.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    /**
     * Guarda una nueva cita después de validar la disponibilidad del estilista.
     *
     * @param nuevaCita La cita a guardar.
     * @return La cita guardada con su ID asignado.
     * @throws IllegalStateException si el estilista no está disponible.
     */
    @Transactional
    public Cita agendarNuevaCita(Cita nuevaCita) throws IllegalStateException {
        // 1. Validar que la cita tenga un estilista asignado
        if (nuevaCita.getEstilista() == null || nuevaCita.getEstilista().getId() == null) {
            throw new IllegalArgumentException("La cita debe tener un estilista asignado.");
        }

        // 2. Comprobar si hay citas que se solapen en el horario del estilista
        List<Cita> citasSolapadas = citaRepository.findCitasSolapadas(
                nuevaCita.getEstilista().getId(),
                nuevaCita.getFechaHoraInicio(),
                nuevaCita.getFechaHoraFin()
        );

        if (!citasSolapadas.isEmpty()) {
            throw new IllegalStateException("El estilista ya tiene una cita en el horario seleccionado.");
        }

        // 3. Establecer el estado inicial y guardar
        nuevaCita.setEstado(EstadoCita.AGENDADA);
        return citaRepository.save(nuevaCita);
    }

    /**
     * Busca todas las citas dentro de un rango de fechas.
     *
     * @param inicio La fecha y hora de inicio del rango.
     * @param fin La fecha y hora de fin del rango.
     * @return Una lista de citas.
     */
    @Transactional(readOnly = true)
    public List<Cita> buscarCitasPorRangoDeFechas(LocalDateTime inicio, LocalDateTime fin) {
        return citaRepository.findByFechaHoraInicioBetween(inicio, fin);
    }

    /**
     * Busca una cita por su ID.
     *
     * @param id El ID de la cita.
     * @return Un Optional que contiene la cita si se encuentra.
     */
    public Optional<Cita> buscarPorId(Long id) {
        return citaRepository.findById(id);
    }

    /**
     * Cancela una cita existente.
     *
     * @param id El ID de la cita a cancelar.
     * @return La cita actualizada con el estado CANCELADA.
     * @throws IllegalStateException si la cita no se encuentra.
     */
    @Transactional
    public Cita cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No se encontró la cita con ID: " + id));

        cita.setEstado(EstadoCita.CANCELADA);
        return citaRepository.save(cita);
    }

    /**
     * Completa una cita existente.
     * @param id El ID de la cita a completar.
     * @return La cita actualizada con el estado COMPLETADA.
     */
    @Transactional
    public Cita completarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No se encontró la cita con ID: " + id));

        cita.setEstado(EstadoCita.COMPLETADA);
        return citaRepository.save(cita);
    }

    /**
     * Confirma una cita existente.
     * @param id El ID de la cita a confirmar.
     * @return La cita actualizada con el estado CONFIRMADA.
     */
    @Transactional
    public Cita confirmarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No se encontró la cita con ID: " + id));

        cita.setEstado(EstadoCita.CONFIRMADA);
        return citaRepository.save(cita);
    }

    /**
     * Busca el historial de citas de un cliente.
     * @param clienteId El ID del cliente.
     * @return La lista de citas del cliente, ordenadas por fecha descendente.
     */
    @Transactional(readOnly = true)
    public List<Cita> buscarCitasPorCliente(Long clienteId) {
        return citaRepository.findByCliente_IdOrderByFechaHoraInicioDesc(clienteId);
    }

    /**
     * Busca las citas que están listas para ser facturadas.
     * @return Lista de citas completadas y sin factura.
     */
    @Transactional(readOnly = true)
    public List<Cita> buscarCitasPendientesDeFacturacion() {
        return citaRepository.findCitasPendientesDeFacturacion();
    }
}