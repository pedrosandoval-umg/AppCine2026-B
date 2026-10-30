
package com.proyecto2026.entity;

import java.util.List;

public class RegistroReserva {

	private List<Integer> asientos;
	public List<Integer> getAsientos() {
		return asientos;
	}

	public void setAsientos(List<Integer> asientos) {
		this.asientos = asientos;
	}

	private Integer idCliente;
	private Integer idFuncion;
	private Integer cantidadEntradas;

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getIdFuncion() {
		return idFuncion;
	}

	public void setIdFuncion(Integer idFuncion) {
		this.idFuncion = idFuncion;
	}

	public Integer getCantidadEntradas() {
		return cantidadEntradas;
	}

	public void setCantidadEntradas(Integer cantidadEntradas) {
		this.cantidadEntradas = cantidadEntradas;
	}
}
