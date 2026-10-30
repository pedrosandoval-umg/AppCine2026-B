
package com.proyecto2026.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto2026.entity.Asiento;
import com.proyecto2026.entity.Cliente;
import com.proyecto2026.entity.Detalle;
import com.proyecto2026.entity.Reserva;

import com.proyecto2026.repository.AsientoRepository;
import com.proyecto2026.repository.ClienteRepository;
import com.proyecto2026.repository.DetalleRepository;
import com.proyecto2026.repository.ReservaRepository;

@Service
public class ConsultaReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final DetalleRepository detalleRepository;
    private final AsientoRepository asientoRepository;

    public ConsultaReservaService(
            ReservaRepository reservaRepository,
            ClienteRepository clienteRepository,
            DetalleRepository detalleRepository,
            AsientoRepository asientoRepository) {

        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.detalleRepository = detalleRepository;
        this.asientoRepository = asientoRepository;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> consultarReservas() {

        List<Reserva> reservas = reservaRepository.findAll();
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Reserva reserva : reservas) {

            Map<String, Object> datos = new HashMap<>();

            Cliente cliente = clienteRepository
                .findById(reserva.getIdCliente())
                .orElse(null);

            List<Detalle> detalles = detalleRepository
                .findByIdReserva(reserva.getIdReserva());

            List<String> asientos = new ArrayList<>();

            for (Detalle detalle : detalles) {

                Asiento asiento = asientoRepository
                    .findById(detalle.getIdAsiento())
                    .orElse(null);

                if (asiento != null) {
                    asientos.add(
                        asiento.getFila() + asiento.getNumero()
                    );
                }
            }

            datos.put("idReserva", reserva.getIdReserva());
            datos.put("idCliente", reserva.getIdCliente());
            datos.put("idFuncion", reserva.getIdFuncion());
            datos.put("cantidadEntradas", reserva.getCantidadEntradas());
            datos.put("totalAPagar", reserva.getTotalAPagar());
            datos.put("fechaReserva", reserva.getFechaReserva());
            datos.put("estado", reserva.isEstado());
            datos.put("asientos", asientos);

            if (cliente != null) {
                datos.put("nombreCliente",
                    cliente.getNombres() + " " +
                    cliente.getApellidos());

                datos.put("nit", cliente.getNit());
            } else {
                datos.put("nombreCliente", "Cliente no encontrado");
                datos.put("nit", "");
            }

            resultado.add(datos);
        }

        return resultado;
    }
}
