package com.example.gymweb.dto.Response;

<<<<<<< Updated upstream
import java.util.List;

public class ClaseResponse {

=======

public class ClaseResponse {
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream

    public Integer getCreadorId() {
        return creadorId;
=======
    public Integer getCreadorId() {
        return this.creadorId;
>>>>>>> Stashed changes
    }

    public void setCreadorId(Integer creadorId) {
        this.creadorId = creadorId;
    }

    public int getCupo() {
<<<<<<< Updated upstream
        return cupo;
=======
        return this.cupo;
>>>>>>> Stashed changes
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public int getId() {
<<<<<<< Updated upstream
        return id;
=======
        return this.id;
>>>>>>> Stashed changes
    }

    public void setId(int id) {
        this.id = id;
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
        return descripcion;
=======
        return this.descripcion;
>>>>>>> Stashed changes
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCreadorNombre() {
<<<<<<< Updated upstream
        return creadorNombre;
=======
        return this.creadorNombre;
>>>>>>> Stashed changes
    }

    public void setCreadorNombre(String creadorNombre) {
        this.creadorNombre = creadorNombre;
    }
}
