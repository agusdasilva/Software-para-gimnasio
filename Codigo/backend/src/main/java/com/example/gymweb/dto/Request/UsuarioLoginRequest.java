package com.example.gymweb.dto.Request;

<<<<<<< Updated upstream
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class UsuarioLoginRequest {

=======

public class UsuarioLoginRequest {
>>>>>>> Stashed changes
    private String email;
    private String password;

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
}
