package com.proyecto2026.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Asiento;

@Repository("asientoRepository")
public interface AsientoRepository extends JpaRepository<Asiento, Integer>{
	
    public List<Asiento> findByIdSala(Integer idSala);

    public List<Asiento> findByIdSalaAndFila(Integer idSala, String fila);

    public boolean existsByIdSalaAndFilaAndNumero(
        Integer idSala,
        String fila,
        Integer numero
    );

}
