
package com.proyecto2026.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.proyecto2026.entity.Cliente;
import com.proyecto2026.entity.RegistroCliente;
import com.proyecto2026.repository.ClienteRepository;

@Service
public class RegistroClienteService {

    private final ClienteRepository clienteRepository;

    public RegistroClienteService(
            ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Map<String, Object> registrar(RegistroCliente datos) {

        if (datos == null ||
            datos.getNit() == null ||
            datos.getNombres() == null ||
            datos.getApellidos() == null ||
            datos.getNit().trim().isEmpty() ||
            datos.getNombres().trim().isEmpty() ||
            datos.getApellidos().trim().isEmpty()) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "NIT, nombres y apellidos son obligatorios."
            );
        }

        String nit = datos.getNit().trim().toUpperCase();

        // Permitir varios clientes CF,
        // pero evitar duplicar otros NIT.
        if (!nit.equals("CF") &&
            clienteRepository.existsByNit(nit)) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Ya existe un cliente con ese NIT."
            );
        }

        Cliente cliente = new Cliente();

        cliente.setNit(nit);
        cliente.setNombres(datos.getNombres().trim());
        cliente.setApellidos(datos.getApellidos().trim());

        cliente.setCorreo(
            datos.getCorreo() == null ||
            datos.getCorreo().trim().isEmpty()
                ? null : datos.getCorreo().trim()
        );

        cliente.setTelefono(
            datos.getTelefono() == null ||
            datos.getTelefono().trim().isEmpty()
                ? null : datos.getTelefono().trim()
        );

        // El cliente presencial no tiene
        // credenciales de acceso a Ionic.
        cliente.setContrasena(null);
        cliente.setFotografia(null);
        cliente.setEstado(true);

        Cliente guardado = clienteRepository.save(cliente);

        Map<String, Object> resultado = new HashMap<>();

        resultado.put("idCliente", guardado.getIdCliente());
        resultado.put("nit", guardado.getNit());
        resultado.put("nombres", guardado.getNombres());
        resultado.put("apellidos", guardado.getApellidos());
        resultado.put("correo", guardado.getCorreo());
        resultado.put("telefono", guardado.getTelefono());

        return resultado;
    }
}
