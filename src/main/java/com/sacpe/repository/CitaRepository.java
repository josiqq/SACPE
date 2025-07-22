package com.sacpe.repository;

import com.sacpe.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    /**
     * Busca todas las citas que se solapan con un intervalo de tiempo
     * para un estilista específico. Esencial para comprobar la disponibilidad.
     *
     * @param estilistaId ID del empleado (estilista).
     * @param fechaHoraInicio Inicio del intervalo a comprobar.
     * @param fechaHoraFin Fin del intervalo a comprobar.
     * @return Una lista de citas que ya existen en ese horario para ese estilista.
     */
    @Query("SELECT c FROM Cita c WHERE c.estilista.id = :estilistaId AND " +
           "c.fechaHoraInicio < :fechaHoraFin AND c.fechaHoraFin > :fechaHoraInicio")
    List<Cita> findCitasSolapadas(
            @Param("estilistaId") Long estilistaId,
            @Param("fechaHoraInicio") LocalDateTime fechaHoraInicio,
            @Param("fechaHoraFin") LocalDateTime fechaHoraFin);

    /**
     * Busca todas las citas dentro de un rango de fechas.
     * Útil para poblar el calendario visual.
     *
     * @param fechaInicio La fecha y hora de inicio del rango.
     * @param fechaFin La fecha y hora de fin del rango.
     * @return Lista de citas en el rango especificado.
     */
    List<Cita> findByFechaHoraInicioBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Busca todas las citas de un cliente específico, ordenadas por fecha descendente.
     * @param clienteId El ID del cliente.
     * @return Una lista de citas del cliente.
     */
    List<Cita> findByCliente_IdOrderByFechaHoraInicioDesc(Long clienteId);

    /**
     * Busca citas que están completadas pero que aún no tienen una factura asociada.
     * @return Lista de citas pendientes de facturar.
     */
    @Query("SELECT c FROM Cita c LEFT JOIN Factura f ON c.id = f.cita.id WHERE c.estado = 'COMPLETADA' AND f.id IS NULL")
    List<Cita> findCitasPendientesDeFacturacion();

}