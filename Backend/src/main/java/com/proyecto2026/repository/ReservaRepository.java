package com.proyecto2026.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Reserva;

@Repository("reservaRepository")
public interface ReservaRepository extends JpaRepository<Reserva, Integer>{
	
    public List<Reserva> findByIdClienteOrderByFechaReservaDesc(Integer idCliente);
    public List<Reserva> findByIdFuncion(Integer idFuncion);
    //public List<Reserva> findByEstadoTrue();

    public List<Reserva> findByFechaReservaBetween(
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin );

}
