package com.example.gymweb.dto.Response;

import java.util.List;

public class ClaseResponse {

    private int id;
    private String titulo;
    private String descripcion;
    private List<UsuarioResponse> usuarios;

    public ClaseResponse(int id, String titulo, String descripcion, List<UsuarioResponse> usuarios) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.usuarios = usuarios;
    }

    public ClaseResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
