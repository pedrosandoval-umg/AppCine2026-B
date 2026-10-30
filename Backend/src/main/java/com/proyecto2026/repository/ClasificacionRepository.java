package com.proyecto2026.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Clasificacion;

@Repository("clasificacionRepository")
public interface ClasificacionRepository extends JpaRepository<Clasificacion, Integer>{
	
    public List<Clasificacion> findByNombre(String nombre);

}