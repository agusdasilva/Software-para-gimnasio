package com.example.gymweb.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ClaseRequest {

    @NotBlank (message = "El titulo no puede estar vacio")
    private String titulo;
    @NotBlank (message = "La descripcion no puede estar vacia")
    private String Descripcion;
    private int cupo;
    private Integer creadorId;

    public ClaseRequest(String titulo, String descripcion, int cupo, Integer creadorId) {
        this.titulo = titulo;
        Descripcion = descripcion;
        this.cupo = cupo;
        this.creadorId = creadorId;
    }

    public Integer getCreadorId() {
        return creadorId;
    }

    public void setCreadorId(Integer creadorId) {
        this.creadorId = creadorId;
    }

    public ClaseRequest() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }


    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }
}
