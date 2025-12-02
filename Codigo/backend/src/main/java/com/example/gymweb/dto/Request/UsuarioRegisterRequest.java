package com.example.gymweb.dto.Request;

import com.example.gymweb.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioRegisterRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 255)
    private String nombre;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 6, max = 255)
    private String password;
    private Rol rol;

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

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return this.rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
