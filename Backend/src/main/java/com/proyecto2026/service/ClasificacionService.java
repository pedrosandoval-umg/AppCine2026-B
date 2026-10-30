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

import com.proyecto2026.entity.Clasificacion;
import com.proyecto2026.repository.ClasificacionRepository;

@RestController
@RequestMapping("/clasificacion")
@CrossOrigin

public class ClasificacionService {
	final ClasificacionRepository clasificacionrepository;

	ClasificacionService(ClasificacionRepository clasificacionrepository) {
		this.clasificacionrepository = clasificacionrepository;
	}
	
	@GetMapping(path="/buscar")
	public List<Clasificacion> buscarclasificacion(){
		return clasificacionrepository.findAll();	
	}
		
	@PostMapping(path="/guardar")
	public Clasificacion guardarclasificacion(@RequestBody Clasificacion clasificacion) {
		return clasificacionrepository.save(clasificacion);
	}

	@DeleteMapping(path="/eliminar/{idClasificacion}")
	public void eliminarclasificacion(@PathVariable Integer idClasificacion){
		clasificacionrepository.deleteById(idClasificacion);
	}
	
	

}
