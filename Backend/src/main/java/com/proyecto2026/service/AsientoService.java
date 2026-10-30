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

import com.proyecto2026.entity.Asiento;
import com.proyecto2026.repository.AsientoRepository;
import com.proyecto2026.entity.AsientoDisponible;

@RestController
@RequestMapping("/asiento")
@CrossOrigin

public class AsientoService {

	final AsientoRepository asientorepository;
	final DisponibilidadAsientoService disponibilidadAsientoService;

	AsientoService(AsientoRepository asientorepository, DisponibilidadAsientoService disponibilidadAsientoService) {

		this.asientorepository = asientorepository;
		this.disponibilidadAsientoService = disponibilidadAsientoService;
	}

	@GetMapping(path = "/buscar")
	public List<Asiento> buscarasiento() {
		return asientorepository.findAll();
	}

	@GetMapping(path = "/buscar/sala/{idSala}")
	public List<Asiento> buscarPorSala(@PathVariable Integer idSala) {
		return asientorepository.findByIdSala(idSala);
	}

	@PostMapping(path = "/guardar")
	public Asiento guardarasiento(@RequestBody Asiento asiento) {

		boolean existe = asientorepository.existsByIdSalaAndFilaAndNumero(asiento.getIdSala(), asiento.getFila(),
				asiento.getNumero());

		if (existe) {
			return null;
		}

		return asientorepository.save(asiento);
	}

	@DeleteMapping(path = "/eliminar/{idAsiento}")
	public void eliminarasiento(@PathVariable Integer idAsiento) {
		asientorepository.deleteById(idAsiento);
	}

	@GetMapping(path = "/disponibles/{idFuncion}")
	public List<AsientoDisponible> consultarDisponibles(@PathVariable Integer idFuncion) {

		return disponibilidadAsientoService.consultarDisponibilidad(idFuncion);
	}
}
