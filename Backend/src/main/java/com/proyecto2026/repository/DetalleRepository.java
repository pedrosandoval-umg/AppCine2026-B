package com.proyecto2026.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Detalle;

@Repository("detalleRepository")
public interface DetalleRepository extends JpaRepository<Detalle, Integer>{

    public List<Detalle> findByIdReserva(Integer idReserva);

    public List<Detalle> findByIdAsiento(Integer idAsiento);
	

}
