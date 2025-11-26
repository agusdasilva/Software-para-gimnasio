package com.example.gymweb.dto.Response;

import java.util.List;

public class RutinaDetalleResponse {
    private int id;
    private String rutina;
    private String descripcion;
    private String imagen;
    private int descanso_seg;
<<<<<<< Updated upstream

    private List<EjercicioDetalleResponse> ejercicios; // empieza vacío

    public int getId() {
        return id;
=======
    private List<EjercicioDetalleResponse> ejercicios;

    public int getId() {
        return this.id;
>>>>>>> Stashed changes
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRutina() {
<<<<<<< Updated upstream
        return rutina;
=======
        return this.rutina;
>>>>>>> Stashed changes
    }

    public void setRutina(String rutina) {
        this.rutina = rutina;
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

    public String getImagen() {
<<<<<<< Updated upstream
        return imagen;
=======
        return this.imagen;
>>>>>>> Stashed changes
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getDescanso_seg() {
<<<<<<< Updated upstream
        return descanso_seg;
=======
        return this.descanso_seg;
>>>>>>> Stashed changes
    }

    public void setDescanso_seg(int descanso_seg) {
        this.descanso_seg = descanso_seg;
    }

    public List<EjercicioDetalleResponse> getEjercicios() {
<<<<<<< Updated upstream
        return ejercicios;
=======
        return this.ejercicios;
>>>>>>> Stashed changes
    }

    public void setEjercicios(List<EjercicioDetalleResponse> ejercicios) {
        this.ejercicios = ejercicios;
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
