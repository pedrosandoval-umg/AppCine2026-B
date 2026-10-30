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

import com.proyecto2026.entity.Pelicula;
import com.proyecto2026.repository.PeliculaRepository;

@RestController
@RequestMapping("/pelicula")
@CrossOrigin

public class PeliculaService {
	final PeliculaRepository pelicularepository;

	PeliculaService(PeliculaRepository pelicularepository) {
		this.pelicularepository = pelicularepository;
	}

	@GetMapping(path = "/buscar")
	public List<Pelicula> buscarpelicula() {
		return pelicularepository.findAll();
	}

	@PostMapping(path = "/guardar")
	public Pelicula guardarpelicula(@RequestBody Pelicula pelicula) {
		return pelicularepository.save(pelicula);
	}

	@DeleteMapping(path = "/eliminar/{idPelicula}")
	public void eliminarpelicula(@PathVariable Integer idPelicula) {
		pelicularepository.deleteById(idPelicula);
	}

	@GetMapping(path = "/activos")
	public List<Pelicula> buscarActivos() {
		return pelicularepository.findByEstadoTrue();
	}

}
