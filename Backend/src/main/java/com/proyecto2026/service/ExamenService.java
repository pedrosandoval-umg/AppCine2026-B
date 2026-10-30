package com.proyecto2026.service;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto2026.entity.Examen;
import com.proyecto2026.repository.ExamenRepository;


@RestController
@RequestMapping("/examen")
@CrossOrigin

public class ExamenService {
	final ExamenRepository examenrepository;

	ExamenService(ExamenRepository examenrepository) {
		this.examenrepository = examenrepository;
	}
	
	@GetMapping(path="/buscar")
	public List<Examen> buscarExamen(){
		return examenrepository.findAll();	
	}
	
	@PostMapping(path="/guardar")
	public Examen guardarExamen(@RequestBody Examen examen) {
		return examenrepository.save(examen);
	}

	@DeleteMapping(path="/eliminar/{idExamen}")
	public void eliminarExamen(@PathVariable Integer idExamen){
		examenrepository.deleteById(idExamen);
	}
	
	@GetMapping(path="/buscar/carne")
	public List<Examen> buscarCarne(){
		return examenrepository.findByCarne(null);	
	}
	
	
}
