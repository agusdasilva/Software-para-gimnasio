package com.example.gymweb.dto.Request;

public class UsuarioPerfilUpdateRequest {
    private String nombre;
    private String descripcion;
    private String fotoUrl;
    private String telefono;
    private String instagram;

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotoUrl() {
        return this.fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getInstagram() {
        return this.instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }
}
