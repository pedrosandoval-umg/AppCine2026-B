
package com.proyecto2026.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.proyecto2026.entity.Asiento;
import com.proyecto2026.entity.AsientoDisponible;
import com.proyecto2026.entity.Detalle;
import com.proyecto2026.entity.Funcion;
import com.proyecto2026.entity.Reserva;

import com.proyecto2026.repository.AsientoRepository;
import com.proyecto2026.repository.DetalleRepository;
import com.proyecto2026.repository.FuncionRepository;
import com.proyecto2026.repository.ReservaRepository;

@Service
public class DisponibilidadAsientoService {

    private final AsientoRepository asientoRepository;
    private final DetalleRepository detalleRepository;
    private final FuncionRepository funcionRepository;
    private final ReservaRepository reservaRepository;

    public DisponibilidadAsientoService(
            AsientoRepository asientoRepository,
            DetalleRepository detalleRepository,
            FuncionRepository funcionRepository,
            ReservaRepository reservaRepository) {

        this.asientoRepository = asientoRepository;
        this.detalleRepository = detalleRepository;
        this.funcionRepository = funcionRepository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional(readOnly = true)
    public List<AsientoDisponible> consultarDisponibilidad(
            Integer idFuncion) {

        Funcion funcion = funcionRepository.findById(idFuncion)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "La función no existe."
            ));

        List<Asiento> asientos = asientoRepository
            .findByIdSala(funcion.getIdSala());

        List<Reserva> reservas = reservaRepository
            .findByIdFuncion(idFuncion);


     // Obtener los identificadores de asientos ocupados
     Set<Integer> ocupados = new HashSet<>();

     for (Reserva reserva : reservas) {

         if (reserva.isEstado()) {

             List<Detalle> detalles = detalleRepository
                 .findByIdReserva(reserva.getIdReserva());

             for (Detalle detalle : detalles) {
                 ocupados.add(detalle.getIdAsiento());
             }
         }
     }


        asientos.sort(
            Comparator.comparing(
                Asiento::getFila,
                Comparator.nullsLast(String::compareTo)
            ).thenComparing(
                Asiento::getNumero,
                Comparator.nullsLast(Integer::compareTo)
            )
        );

        List<AsientoDisponible> resultado = new ArrayList<>();

        for (Asiento asiento : asientos) {

            boolean disponible =
                !ocupados.contains(asiento.getIdAsiento());

            resultado.add(new AsientoDisponible(
                asiento.getIdAsiento(),
                asiento.getFila(),
                asiento.getNumero(),
                asiento.getIdSala(),
                disponible
            ));
        }

        return resultado;
    }
}
