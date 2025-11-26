package com.example.gymweb.dto.Response;


public class ClaseResponse {
    private int id;
    private String titulo;
    private String descripcion;
    private int cupo;
    private Integer creadorId;
    private String creadorNombre;

    public ClaseResponse(int id, String titulo, String descripcion, int cupo, Integer creadorId, String creadorNombre) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.cupo = cupo;
        this.creadorId = creadorId;
        this.creadorNombre = creadorNombre;
    }

    public ClaseResponse() {
    }

    public Integer getCreadorId() {
        return this.creadorId;
    }

    public void setCreadorId(Integer creadorId) {
        this.creadorId = creadorId;
    }

    public int getCupo() {
        return this.cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCreadorNombre() {
        return this.creadorNombre;
    }

    public void setCreadorNombre(String creadorNombre) {
        this.creadorNombre = creadorNombre;
    }
}
