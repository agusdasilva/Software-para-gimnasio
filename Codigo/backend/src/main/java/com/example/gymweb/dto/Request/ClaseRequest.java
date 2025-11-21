package com.example.gymweb.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ClaseRequest {

    @NotBlank (message = "El titulo no puede estar vacio")
    private String titulo;
    @NotBlank (message = "La descripcion no puede estar vacia")
    private String Descripcion;

    public ClaseRequest(String titulo, String descripcion) {
        this.titulo = titulo;
        Descripcion = descripcion;
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
}
