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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.ArrayList;

import com.proyecto2026.entity.Cliente;
import com.proyecto2026.repository.ClienteRepository;
import com.proyecto2026.entity.RegistroCliente;

@RestController
@RequestMapping("/cliente")
@CrossOrigin

public class ClienteService {
	final ClienteRepository clienterepository;
	final RegistroClienteService registroClienteService;

	ClienteService(ClienteRepository clienterepository, RegistroClienteService registroClienteService) {

		this.clienterepository = clienterepository;
		this.registroClienteService = registroClienteService;
	}

	@GetMapping(path = "/buscar")
	public List<Cliente> buscarcliente() {
		return clienterepository.findAll();
	}

	@GetMapping(path = "/buscar/filtro")
	public List<Map<String, Object>> buscarClientes(@RequestParam String criterio, @RequestParam String valor) {

		if (valor == null || valor.trim().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe ingresar un valor de búsqueda.");
		}

		String busqueda = valor.trim();
		List<Cliente> clientes;

		switch (criterio.toLowerCase()) {

		case "nit":
			clientes = clienterepository.findByNit(busqueda);
			break;

		case "nombre":
			clientes = clienterepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(busqueda,
					busqueda);
			break;

		case "telefono":
			clientes = clienterepository.findByTelefonoContaining(busqueda);
			break;

		case "correo":
			clientes = clienterepository.findByCorreoContainingIgnoreCase(busqueda);
			break;

		default:
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Criterio de búsqueda no válido.");
		}

		List<Map<String, Object>> resultado = new ArrayList<>();

		for (Cliente cliente : clientes) {

			Map<String, Object> datos = new java.util.HashMap<>();

			datos.put("idCliente", cliente.getIdCliente());
			datos.put("nit", cliente.getNit());
			datos.put("nombres", cliente.getNombres());
			datos.put("apellidos", cliente.getApellidos());
			datos.put("telefono", cliente.getTelefono());
			datos.put("correo", cliente.getCorreo());

			resultado.add(datos);
		}

		return resultado;
	}

	@PostMapping(path = "/guardar")
	public Cliente guardarcliente(@RequestBody Cliente cliente) {
		return clienterepository.save(cliente);
	}

	@DeleteMapping(path = "/eliminar/{idCliente}")
	public void eliminarcliente(@PathVariable Integer idCliente) {
		clienterepository.deleteById(idCliente);
	}

	@PostMapping(path = "/registrar-rapido")
	public Map<String, Object> registrarRapido(@RequestBody RegistroCliente datos) {

		return registroClienteService.registrar(datos);
	}

	@PostMapping("/login")
	public Cliente login(@RequestBody Cliente datos) {

		List<Cliente> clientes = clienterepository.findByCorreoAndContrasena(datos.getCorreo(), datos.getContrasena());

		if (!clientes.isEmpty()) {
			Cliente cliente = clientes.get(0);
			cliente.setContrasena(null);
			return cliente;
		}

		return null;
	}

}
