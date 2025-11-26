package com.example.gymweb.dto.Request;

import jakarta.validation.constraints.NotBlank;
<<<<<<< Updated upstream
import jakarta.validation.constraints.NotNull;

public class ClaseRequest {

    @NotBlank (message = "El titulo no puede estar vacio")
    private String titulo;
    @NotBlank (message = "La descripcion no puede estar vacia")
    private String Descripcion;
=======

public class ClaseRequest {
    private @NotBlank(
            message = "El titulo no puede estar vacio"
    ) String titulo;
    private @NotBlank(
            message = "La descripcion no puede estar vacia"
    ) String Descripcion;
>>>>>>> Stashed changes
    private int cupo;
    private Integer creadorId;

    public ClaseRequest(String titulo, String descripcion, int cupo, Integer creadorId) {
        this.titulo = titulo;
<<<<<<< Updated upstream
        Descripcion = descripcion;
=======
        this.Descripcion = descripcion;
>>>>>>> Stashed changes
        this.cupo = cupo;
        this.creadorId = creadorId;
    }

    public Integer getCreadorId() {
<<<<<<< Updated upstream
        return creadorId;
=======
        return this.creadorId;
>>>>>>> Stashed changes
    }

    public void setCreadorId(Integer creadorId) {
        this.creadorId = creadorId;
    }

    public ClaseRequest() {
    }

    public String getTitulo() {
<<<<<<< Updated upstream
        return titulo;
=======
        return this.titulo;
>>>>>>> Stashed changes
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
<<<<<<< Updated upstream
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }


    public int getCupo() {
        return cupo;
=======
        return this.Descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.Descripcion = descripcion;
    }

    public int getCupo() {
        return this.cupo;
>>>>>>> Stashed changes
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }
}
