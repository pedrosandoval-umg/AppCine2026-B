
package com.proyecto2026.service;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto2026.entity.Detalle;
import com.proyecto2026.repository.DetalleRepository;

@RestController
@RequestMapping("/detalle")
@CrossOrigin
public class DetalleService {

    final DetalleRepository detallerepository;

    DetalleService(DetalleRepository detallerepository) {
        this.detallerepository = detallerepository;
    }

    @GetMapping(path="/buscar")
    public List<Detalle> buscardetalle() {
        return detallerepository.findAll();
    }
}
