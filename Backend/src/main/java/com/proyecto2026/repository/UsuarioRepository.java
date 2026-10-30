package com.proyecto2026.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto2026.entity.Usuario;

@Repository("usuarioRepository")
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

	public List<Usuario> findByNombresAndApellidos(String nombres, String apellidos);
	public List<Usuario> findByCorreoAndContrasena(String correo, String contrasena);
	public List<Usuario> findByUsuarioAndContrasena(String usuario, String contrasena);
	

    public List<Usuario> findByEstadoTrue();
    public List<Usuario> findByIdRol(Integer idRol);

    public boolean existsByCorreo(String correo);
    public boolean existsByUsuario(String usuario);
    
}
