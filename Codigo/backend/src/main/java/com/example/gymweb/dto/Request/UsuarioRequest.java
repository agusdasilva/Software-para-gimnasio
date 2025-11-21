package com.example.gymweb.dto.Request;

import com.example.gymweb.model.Estado;
import com.example.gymweb.model.Rol;

import java.util.Date;

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
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }
}
