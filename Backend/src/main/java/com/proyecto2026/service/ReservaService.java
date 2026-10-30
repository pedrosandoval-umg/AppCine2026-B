
package com.proyecto2026.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto2026.entity.Reserva;
import com.proyecto2026.entity.RegistroReserva;
import com.proyecto2026.repository.ReservaRepository;

@RestController
@RequestMapping("/reserva")
@CrossOrigin
public class ReservaService {

	final ReservaRepository reservarepository;
	final RegistroReservaService registroReservaService;
	final ConsultaReservaService consultaReservaService;

	ReservaService(ReservaRepository reservarepository, RegistroReservaService registroReservaService,
			ConsultaReservaService consultaReservaService) {

		this.reservarepository = reservarepository;
		this.registroReservaService = registroReservaService;
		this.consultaReservaService = consultaReservaService;
	}

	@GetMapping(path = "/buscar")
	public List<Reserva> buscarreserva() {
		return reservarepository.findAll();
	}

	@GetMapping(path = "/buscar/detalle")
	public List<Map<String, Object>> buscarReservasDetalle() {
		return consultaReservaService.consultarReservas();
	}

	@PostMapping(path = "/registrar")
	public Reserva registrarReserva(@RequestBody RegistroReserva datos) {

		return registroReservaService.registrar(datos);
	}

	@PutMapping(path = "/cancelar/{idReserva}")
	public Reserva cancelarReserva(@PathVariable Integer idReserva) {

		return registroReservaService.cancelar(idReserva);
	}
}
