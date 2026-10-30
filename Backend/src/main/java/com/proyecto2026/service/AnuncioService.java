package com.proyecto2026.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.proyecto2026.entity.Anuncio;
import com.proyecto2026.repository.AnuncioRepository;

@RestController
@RequestMapping("/anuncio")
@CrossOrigin

public class AnuncioService {
	final AnuncioRepository anunciorepository;

	AnuncioService(AnuncioRepository anunciorepository) {
		this.anunciorepository = anunciorepository;
	}

	@GetMapping(path = "/buscar")
	public List<Anuncio> buscaranuncio() {
		return anunciorepository.findAll();
	}

	@GetMapping(path = "/activos")
	public List<Anuncio> buscarActivos() {
		return anunciorepository.findByEstadoTrueOrderByFechaPublicacionDesc();
	}

	@PostMapping(path = "/guardar")
	public Anuncio guardarAnuncio(@RequestBody Anuncio anuncio) {

		// NUEVO ANUNCIO
		if (anuncio.getIdAnuncio() == null) {

			anuncio.setFechaPublicacion(LocalDateTime.now());

			return anunciorepository.save(anuncio);
		}

		// EDITAR ANUNCIO EXISTENTE
		Anuncio existente = anunciorepository.findById(anuncio.getIdAnuncio())
				.orElseThrow(() -> new RuntimeException("El anuncio no existe."));

		existente.setTitulo(anuncio.getTitulo());
		existente.setDescripcion(anuncio.getDescripcion());
		existente.setTipo(anuncio.getTipo());
		existente.setTipo2(anuncio.getTipo2());

		// Solo reemplazar la imagen si se seleccionó otra
		if (anuncio.getImagen() != null && !anuncio.getImagen().isEmpty()) {

			existente.setImagen(anuncio.getImagen());
		}

		return anunciorepository.save(existente);
	}

	@DeleteMapping(path = "/eliminar/{idAnuncio}")
	public void eliminarAnuncio(@PathVariable Integer idAnuncio) {
		anunciorepository.deleteById(idAnuncio);
	}

	@PutMapping(path = "/estado/{idAnuncio}")
	public Anuncio cambiarEstado(@PathVariable Integer idAnuncio) {

		Anuncio anuncio = anunciorepository.findById(idAnuncio)
				.orElseThrow(() -> new RuntimeException("El anuncio no existe."));

		anuncio.setEstado(!anuncio.isEstado());

		return anunciorepository.save(anuncio);
	}

}
