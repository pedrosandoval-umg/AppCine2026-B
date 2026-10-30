package com.proyecto2026.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Cliente;

@Repository("clienteRepository")
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	public List<Cliente> findByNombresAndApellidos(String nombres, String apellidos);

	public List<Cliente> findByCorreoAndContrasena(String correo, String contrasena);

	public List<Cliente> findByNitAndContrasena(String nit, String contrasena);

	public List<Cliente> findByNit(String nit);
	
	public List<Cliente> findByCorreo(String correo);

	public List<Cliente> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombres,
			String apellidos);

	public List<Cliente> findByTelefonoContaining(String telefono);

	public List<Cliente> findByCorreoContainingIgnoreCase(String correo);

	public boolean existsByNit(String nit);

	public boolean existsByCorreo(String correo);

}
