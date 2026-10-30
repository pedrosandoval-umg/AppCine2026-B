
package com.proyecto2026.entity;

public class AsientoDisponible {

    private Integer idAsiento;
    private String fila;
    private Integer numero;
    private Integer idSala;
    private boolean disponible;

    public AsientoDisponible(
            Integer idAsiento,
            String fila,
            Integer numero,
            Integer idSala,
            boolean disponible) {

        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
        this.idSala = idSala;
        this.disponible = disponible;
    }

    public Integer getIdAsiento() {
        return idAsiento;
    }

    public String getFila() {
        return fila;
    }

    public Integer getNumero() {
        return numero;
    }

    public Integer getIdSala() {
        return idSala;
    }

    public boolean isDisponible() {
        return disponible;
    }
}
