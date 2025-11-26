package com.example.gymweb.dto.Request;

import jakarta.validation.constraints.NotBlank;

public class ClaseRequest {
    private @NotBlank(
            message = "El titulo no puede estar vacio"
    ) String titulo;
    private @NotBlank(
            message = "La descripcion no puede estar vacia"
    ) String Descripcion;
    private int cupo;
    private Integer creadorId;

    public ClaseRequest(String titulo, String descripcion, int cupo, Integer creadorId) {
        this.titulo = titulo;
        this.Descripcion = descripcion;
        this.cupo = cupo;
        this.creadorId = creadorId;
    }

    public Integer getCreadorId() {
        return this.creadorId;
    }

    public void setCreadorId(Integer creadorId) {
        this.creadorId = creadorId;
    }

    public ClaseRequest() {
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return this.Descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.Descripcion = descripcion;
    }

    public int getCupo() {
        return this.cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }
}
