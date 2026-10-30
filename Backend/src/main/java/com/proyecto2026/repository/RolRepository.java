package com.proyecto2026.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Rol;

@Repository("rolRepository")
public interface RolRepository extends JpaRepository<Rol, Integer>{
	
    public List<Rol> findByNombre(String nombre);


}
