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

import com.proyecto2026.entity.Promocion;
import com.proyecto2026.repository.PromocionRepository;


@RestController
@RequestMapping("/promocion")
@CrossOrigin

public class PromocionService {
	
	final PromocionRepository promocionrepository;

	PromocionService(PromocionRepository promocionrepository) {
		this.promocionrepository = promocionrepository;
	}
	
	@GetMapping(path="/buscar")
	public List<Promocion> buscarpromocion(){
		return promocionrepository.findAll();	
	}
	
	@PostMapping(path="/guardar")
	public Promocion guardarPromocion(@RequestBody Promocion promocion) {
		return promocionrepository.save(promocion);
	}

	@DeleteMapping(path="/eliminar/{idPromocion}")
	public void eliminarPromocion(@PathVariable Integer idPromocion){
		promocionrepository.deleteById(idPromocion);
	}

	
	
	

}
