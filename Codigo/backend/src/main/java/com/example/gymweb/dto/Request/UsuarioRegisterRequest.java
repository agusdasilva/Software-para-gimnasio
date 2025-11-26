package com.example.gymweb.dto.Request;

<<<<<<< Updated upstream
import com.example.gymweb.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioRegisterRequest {

    private String nombre;
    private String email;
    private String password;
    private Rol rol; // Si viene null, lo dejamos por defecto CLIENTE

    public String getNombre() {
        return nombre;
=======

import com.example.gymweb.model.Rol;

public class UsuarioRegisterRequest {
    private String nombre;
    private String email;
    private String password;
    private Rol rol;

    public String getNombre() {
        return this.nombre;
>>>>>>> Stashed changes
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
<<<<<<< Updated upstream
        return email;
=======
        return this.email;
>>>>>>> Stashed changes
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
<<<<<<< Updated upstream
        return password;
=======
        return this.password;
>>>>>>> Stashed changes
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
<<<<<<< Updated upstream
        return rol;
=======
        return this.rol;
>>>>>>> Stashed changes
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
