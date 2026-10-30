package com.proyecto2026.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Anuncio;

@Repository("anuncioRepository")
public interface AnuncioRepository extends JpaRepository<Anuncio, Integer>{
	
	public List<Anuncio> findByTitulo(String titulo);

	public List<Anuncio> findByEstadoTrueOrderByFechaPublicacionDesc();
	
}
