package com.proyecto2026.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Promocion;

@Repository("promocionRepository")
public interface PromocionRepository extends JpaRepository<Promocion, Integer>  {
	
	public List<Promocion> findByTitulo(String titulo);
	public List<Promocion> findByEstadoTrueOrderByFechaInicioDesc();



}
