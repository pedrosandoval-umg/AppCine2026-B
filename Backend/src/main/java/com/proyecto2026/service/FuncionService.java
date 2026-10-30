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

import com.proyecto2026.entity.Funcion;
import com.proyecto2026.repository.FuncionRepository;

@RestController
@RequestMapping("/funcion")
@CrossOrigin
public class FuncionService {
	final FuncionRepository funcionrepository;

	FuncionService(FuncionRepository funcionrepository) {
		this.funcionrepository = funcionrepository;
	}

	@GetMapping(path="/buscar")
	public List<Funcion> buscarfuncion(){
		return funcionrepository.findAll();	
	}
		
	@PostMapping(path="/guardar")
	public Funcion guardarfuncion(@RequestBody Funcion funcion) {
		return funcionrepository.save(funcion);
	}

	@DeleteMapping(path="/eliminar/{idFuncion}")
	public void eliminarfuncion(@PathVariable Integer idFuncion){
		funcionrepository.deleteById(idFuncion);
	}

}
