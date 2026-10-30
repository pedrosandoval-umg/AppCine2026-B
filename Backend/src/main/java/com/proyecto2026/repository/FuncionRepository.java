package com.proyecto2026.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Funcion;

@Repository("funcionRepository")
public interface FuncionRepository extends JpaRepository<Funcion, Integer> {

	public List<Funcion> findByIdPelicula(Integer idPelicula);

	public List<Funcion> findByIdSala(Integer idSala);

	public List<Funcion> findByFecha(LocalDate fecha);

	public List<Funcion> findByEstadoTrue();

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT f FROM Funcion f WHERE f.idFuncion = :idFuncion")
	Optional<Funcion> bloquearFuncion(@Param("idFuncion") Integer idFuncion);

}
