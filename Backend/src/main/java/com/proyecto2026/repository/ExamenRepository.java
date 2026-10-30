package com.proyecto2026.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Examen;

@Repository("examenRepository")
public interface ExamenRepository extends JpaRepository<Examen, Integer>{
	
	public List<Examen> findByCarne(String carne);


}
