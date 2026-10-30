package com.proyecto2026.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Pelicula;

@Repository("peliculaRepository")
public interface PeliculaRepository extends JpaRepository<Pelicula, Integer>{
	
    public List<Pelicula> findByTitulo(String titulo);

    public List<Pelicula> findByIdGenero(Integer idGenero);

    public List<Pelicula> findByIdClasificacion(Integer idClasificacion);

    public List<Pelicula> findByEstadoTrue();

}
