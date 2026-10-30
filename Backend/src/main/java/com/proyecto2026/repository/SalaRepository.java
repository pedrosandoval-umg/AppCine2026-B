package com.proyecto2026.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Sala;

@Repository("salaRepository")
public interface SalaRepository extends JpaRepository<Sala, Integer>{

    public List<Sala> findByEstadoTrue();
    public boolean existsByNombre(String nombre);

}
