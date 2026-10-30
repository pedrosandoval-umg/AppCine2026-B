package com.proyecto2026.entity;

import java.io.Serializable;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "sala")
public class Sala implements Serializable{
	private static final long serialVersionUID = 954315835759247775L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_sala")
	private Integer idSala;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "filas")
	private Integer filas;
	
	@Column(name = "asientos_por_fila")
	private Integer asientosPorFila;

	@Column(name = "estado", nullable = false)
	private boolean estado = true;


	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Integer getIdSala() {
		return idSala;
	}

	public void setIdSala(Integer idSala) {
		this.idSala = idSala;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getFilas() {
		return filas;
	}

	public void setFilas(Integer filas) {
		this.filas = filas;
	}

	public Integer getAsientosPorFila() {
		return asientosPorFila;
	}

	public void setAsientosPorFila(Integer asientosPorFila) {
		this.asientosPorFila = asientosPorFila;
	}


}
