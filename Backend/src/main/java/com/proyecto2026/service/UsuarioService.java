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

import com.proyecto2026.entity.Usuario;
import com.proyecto2026.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuario")
@CrossOrigin

public class UsuarioService {
	final UsuarioRepository usuariorepository;

	UsuarioService(UsuarioRepository usuariorepository) {
		this.usuariorepository = usuariorepository;
	}

	@GetMapping(path = "/buscar")
	public List<Usuario> buscarusuario() {
		return usuariorepository.findAll();
	}

	@GetMapping(path = "/buscar/{nombres}/{apellidos}")
	public List<Usuario> buscarusuarioPorNombreyApellido(@PathVariable("nombres") String nombres,
			@PathVariable("apellidos") String apellidos) {
		return usuariorepository.findByNombresAndApellidos(nombres, apellidos);
	}

	@PostMapping(path = "/guardar")
	public Usuario guardarusuario(@RequestBody Usuario usuario) {
		return usuariorepository.save(usuario);
	}

	@DeleteMapping(path = "/eliminar/{idUsuario}")
	public void eliminarusuario(@PathVariable Integer idUsuario) {
		usuariorepository.deleteById(idUsuario);
	}
		
	@PostMapping(path = "/login")
	public Usuario login(@RequestBody Usuario usuario) {

	    List<Usuario> usuarios =
	        usuariorepository.findByUsuarioAndContrasena(usuario.getUsuario(),usuario.getContrasena());
	    if (usuarios.isEmpty()) {
	        return null;
	    }
	    return usuarios.get(0);
	}
}
