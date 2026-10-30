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

import com.proyecto2026.entity.Rol;
import com.proyecto2026.repository.RolRepository;

@RestController
@RequestMapping("/rol")
@CrossOrigin

public class RolService {
	final RolRepository rolrepository;

	RolService(RolRepository rolrepository) {
		this.rolrepository = rolrepository;
	}
	
	@GetMapping(path="/buscar")
	public List<Rol> buscarrol(){
		return rolrepository.findAll();	
	}
		
	@PostMapping(path="/guardar")
	public Rol guardarrol(@RequestBody Rol rol) {
		return rolrepository.save(rol);
	}

	@DeleteMapping(path="/eliminar/{idRol}")
	public void eliminarrol(@PathVariable Integer idRol){
		rolrepository.deleteById(idRol);
	}
	
}
