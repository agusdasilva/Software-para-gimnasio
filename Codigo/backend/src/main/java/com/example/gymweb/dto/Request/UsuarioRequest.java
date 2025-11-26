package com.example.gymweb.dto.Request;

public class UsuarioRequest {
    private String nombre;
    private String email;
    private String password_hash;

    public UsuarioRequest(String nombre, String email, String password_hash) {
        this.nombre = nombre;
        this.email = email;
        this.password_hash = password_hash;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return this.password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }
}
