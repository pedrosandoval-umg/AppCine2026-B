
package com.proyecto2026.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.proyecto2026.entity.Asiento;
import com.proyecto2026.entity.Detalle;
import com.proyecto2026.entity.Funcion;
import com.proyecto2026.entity.RegistroReserva;
import com.proyecto2026.entity.Reserva;

import com.proyecto2026.repository.AsientoRepository;
import com.proyecto2026.repository.ClienteRepository;
import com.proyecto2026.repository.DetalleRepository;
import com.proyecto2026.repository.FuncionRepository;
import com.proyecto2026.repository.ReservaRepository;

@Service
public class RegistroReservaService {

	private final ReservaRepository reservaRepository;
	private final DetalleRepository detalleRepository;
	private final FuncionRepository funcionRepository;
	private final ClienteRepository clienteRepository;
	private final AsientoRepository asientoRepository;

	public RegistroReservaService(ReservaRepository reservaRepository, DetalleRepository detalleRepository,
			FuncionRepository funcionRepository, ClienteRepository clienteRepository,
			AsientoRepository asientoRepository) {

		this.reservaRepository = reservaRepository;
		this.detalleRepository = detalleRepository;
		this.funcionRepository = funcionRepository;
		this.clienteRepository = clienteRepository;
		this.asientoRepository = asientoRepository;
	}

	@Transactional
	public Reserva registrar(RegistroReserva datos) {

		// Validar los datos recibidos
		if (datos == null || datos.getIdCliente() == null || datos.getIdFuncion() == null
				|| datos.getCantidadEntradas() == null || datos.getCantidadEntradas() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos de reserva incompletos.");
		}

		// Verificar que exista el cliente
		if (!clienteRepository.existsById(datos.getIdCliente())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente no existe.");
		}

		// Obtener y bloquear la función
		Funcion funcion = funcionRepository.bloquearFuncion(datos.getIdFuncion())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La función no existe."));
		if (funcion.getIdSala() == null || funcion.getPrecioEntrada() == null || funcion.getPrecioEntrada() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La función no tiene sala o precio válido.");
		}

		// Obtener todos los asientos de la sala
		List<Asiento> asientos = asientoRepository.findByIdSala(funcion.getIdSala());

		// Ordenar por fila y número
		asientos.sort(Comparator.comparing(Asiento::getFila, Comparator.nullsLast(String::compareTo))
				.thenComparing(Asiento::getNumero, Comparator.nullsLast(Integer::compareTo)));

		// Buscar las reservas de esta función
		List<Reserva> reservas = reservaRepository.findByIdFuncion(datos.getIdFuncion());

		// Identificar los asientos ocupados
		Set<Integer> ocupados = new HashSet<>();

		for (Reserva reserva : reservas) {

			if (reserva.isEstado()) {

				List<Detalle> detalles = detalleRepository.findByIdReserva(reserva.getIdReserva());

				for (Detalle detalle : detalles) {
					ocupados.add(detalle.getIdAsiento());
				}
			}
		}

		// Obtener los asientos disponibles
		List<Asiento> disponibles = new ArrayList<>();
		for (Asiento asiento : asientos) {
			if (!ocupados.contains(asiento.getIdAsiento())) {
				disponibles.add(asiento);
			}
		}

		// Lista de asientos que se asignarán
		List<Asiento> seleccionados = new ArrayList<>();
		// Verificar si el usuario seleccionó asientos manualmente
		if (datos.getAsientos() != null && !datos.getAsientos().isEmpty()) {
			// Validar que la cantidad coincida
			if (datos.getAsientos().size() != datos.getCantidadEntradas()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"La cantidad de asientos no coincide con las entradas.");
			}
			// Evitar asientos repetidos en la solicitud
			Set<Integer> idsSolicitados = new HashSet<>(datos.getAsientos());
			if (idsSolicitados.size() != datos.getAsientos().size() || idsSolicitados.contains(null)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"La selección contiene asientos repetidos o inválidos.");
			}

			// Buscar los asientos de la sala por identificador
			Map<Integer, Asiento> asientosSala = new HashMap<>();
			for (Asiento asiento : asientos) {
				asientosSala.put(asiento.getIdAsiento(), asiento);
			}

			// Validar cada asiento solicitado
			for (Integer idAsiento : datos.getAsientos()) {
				Asiento asiento = asientosSala.get(idAsiento);
				// Comprobar que pertenezca a la sala de la función
				if (asiento == null) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"El asiento " + idAsiento + " no pertenece a la sala de esta función.");
				}

				// Comprobar que no esté reservado
				if (ocupados.contains(idAsiento)) {
					throw new ResponseStatusException(HttpStatus.CONFLICT,
							"El asiento " + idAsiento + " ya está reservado para esta función.");
				}

				seleccionados.add(asiento);
			}

		} else {

			// Asignación automática
			if (disponibles.size() < datos.getCantidadEntradas()) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "No hay suficientes asientos disponibles.");
			}

			for (int i = 0; i < datos.getCantidadEntradas(); i++) {
				seleccionados.add(disponibles.get(i));
			}
		}

		// Crear la reserva
		Reserva reserva = new Reserva();

		reserva.setIdCliente(datos.getIdCliente());
		reserva.setIdFuncion(datos.getIdFuncion());
		reserva.setCantidadEntradas(datos.getCantidadEntradas());
		reserva.setFechaReserva(LocalDateTime.now());
		reserva.setEstado(true);

		double total = funcion.getPrecioEntrada() * datos.getCantidadEntradas();

		reserva.setTotalAPagar(total);

		// Guardar primero la reserva
		Reserva reservaGuardada = reservaRepository.save(reserva);

		// Registrar los asientos asignados
		for (int i = 0; i < datos.getCantidadEntradas(); i++) {
			Asiento asiento = seleccionados.get(i);
			Detalle detalle = new Detalle();
			detalle.setIdReserva(reservaGuardada.getIdReserva());
			detalle.setIdAsiento(asiento.getIdAsiento());
			detalleRepository.save(detalle);
		}

		return reservaGuardada;
	}

	@Transactional
	public Reserva cancelar(Integer idReserva) {

		Reserva reserva = reservaRepository.findById(idReserva)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La reserva no existe."));

		// Bloquear la función mientras se cancela
		funcionRepository.bloquearFuncion(reserva.getIdFuncion())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La función no existe."));

		// Volver a consultar la reserva después del bloqueo
		reserva = reservaRepository.findById(idReserva)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La reserva no existe."));

		if (!reserva.isEstado()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "La reserva ya está cancelada.");
		}

		reserva.setEstado(false);

		return reservaRepository.save(reserva);
	}

}
