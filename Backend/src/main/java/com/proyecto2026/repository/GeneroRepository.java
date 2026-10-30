package com.proyecto2026.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Genero;

@Repository("generoRepository")
public interface GeneroRepository extends JpaRepository<Genero, Integer>{
	
    public List<Genero> findByNombre(String nombre);

}