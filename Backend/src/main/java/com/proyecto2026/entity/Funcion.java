package com.proyecto2026.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcion")
public class Funcion implements Serializable{
	private static final long serialVersionUID = -4111251217477288892L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_funcion")
	private Integer idFuncion;
	
	@Column(name = "fecha")
	private LocalDate fecha;
	
	@Column(name = "hora")
	private LocalTime hora;

	@Column(name = "precio_entrada")
	private Double precioEntrada;
	
	@Column(name = "id_pelicula")
	private Integer idPelicula;
	
	@Column(name = "id_sala")
	private Integer idSala;
	
	@Column(name = "estado", nullable = false)
	private boolean estado = true;


	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Integer getIdFuncion() {
		return idFuncion;
	}
	

	public void setIdFuncion(Integer idFuncion) {
		this.idFuncion = idFuncion;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public Double getPrecioEntrada() {
		return precioEntrada;
	}

	public void setPrecioEntrada(Double precioEntrada) {
		this.precioEntrada = precioEntrada;
	}

	public Integer getIdPelicula() {
		return idPelicula;
	}

	public void setIdPelicula(Integer idPelicula) {
		this.idPelicula = idPelicula;
	}

	public Integer getIdSala() {
		return idSala;
	}

	public void setIdSala(Integer idSala) {
		this.idSala = idSala;
	}
	

}
