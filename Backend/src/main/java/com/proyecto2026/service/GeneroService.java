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

import com.proyecto2026.entity.Genero;
import com.proyecto2026.repository.GeneroRepository;

@RestController
@RequestMapping("/genero")
@CrossOrigin

public class GeneroService {
	final GeneroRepository generorepository;

	GeneroService(GeneroRepository generorepository) {
		this.generorepository = generorepository;
	}
	
	@GetMapping(path="/buscar")
	public List<Genero> buscargenero(){
		return generorepository.findAll();	
	}
		
	@PostMapping(path="/guardar")
	public Genero guardargenero(@RequestBody Genero genero) {
		return generorepository.save(genero);
	}

	@DeleteMapping(path="/eliminar/{idGenero}")
	public void eliminargenero(@PathVariable Integer idGenero){
		generorepository.deleteById(idGenero);
	}
	

}
