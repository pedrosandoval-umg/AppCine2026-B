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

import com.proyecto2026.entity.Sala;
import com.proyecto2026.repository.SalaRepository;

@RestController
@RequestMapping("/sala")
@CrossOrigin
public class SalaService {
	
	final SalaRepository salarepository;

	SalaService(SalaRepository salarepository) {
		this.salarepository = salarepository;
	}

	@GetMapping(path="/buscar")
	public List<Sala> buscarsala(){
		return salarepository.findAll();	
	}
		
	@PostMapping(path="/guardar")
	public Sala guardarsala(@RequestBody Sala sala) {
		return salarepository.save(sala);
	}

	@DeleteMapping(path="/eliminar/{idSala}")
	public void eliminarsala(@PathVariable Integer idSala){
		salarepository.deleteById(idSala);
	}

}
