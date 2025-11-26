package com.example.gymweb.dto.Response;

<<<<<<< Updated upstream
import com.example.gymweb.model.Rol;

public class AuthResponse {

=======

import com.example.gymweb.model.Rol;

public class AuthResponse {
>>>>>>> Stashed changes
    private String token;
    private Integer id;
    private String email;
    private Rol rol;

    public String getToken() {
<<<<<<< Updated upstream
        return token;
=======
        return this.token;
>>>>>>> Stashed changes
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
<<<<<<< Updated upstream
        return id;
=======
        return this.id;
>>>>>>> Stashed changes
    }

    public void setId(Integer id) {
        this.id = id;
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
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
