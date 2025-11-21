package com.example.gymweb.dto.Response;

import java.util.List;

public class RutinaDetalleResponse {
    private int id;
    private String rutina;
    private String descripcion;
    private String imagen;
    private int descanso_seg;

    private List<EjercicioDetalleResponse> ejercicios; // empieza vacío

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRutina() {
        return rutina;
    }

    public void setRutina(String rutina) {
        this.rutina = rutina;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getDescanso_seg() {
        return descanso_seg;
    }

    public void setDescanso_seg(int descanso_seg) {
        this.descanso_seg = descanso_seg;
    }

    public List<EjercicioDetalleResponse> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<EjercicioDetalleResponse> ejercicios) {
        this.ejercicios = ejercicios;
    }
}