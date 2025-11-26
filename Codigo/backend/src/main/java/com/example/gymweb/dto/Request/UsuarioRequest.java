package com.example.gymweb.dto.Request;

<<<<<<< Updated upstream
import com.example.gymweb.model.Estado;
import com.example.gymweb.model.Rol;

import java.util.Date;

public class UsuarioRequest {

=======
public class UsuarioRequest {
>>>>>>> Stashed changes
    private String nombre;
    private String email;
    private String password_hash;

    public UsuarioRequest(String nombre, String email, String password_hash) {
        this.nombre = nombre;
        this.email = email;
        this.password_hash = password_hash;
    }

    public String getNombre() {
<<<<<<< Updated upstream
        return nombre;
=======
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

    public String getPassword_hash() {
<<<<<<< Updated upstream
        return password_hash;
=======
        return this.password_hash;
>>>>>>> Stashed changes
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }
}
