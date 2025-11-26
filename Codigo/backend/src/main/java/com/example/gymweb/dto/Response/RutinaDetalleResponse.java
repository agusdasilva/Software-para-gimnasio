package com.example.gymweb.dto.Response;

import java.util.List;

public class RutinaDetalleResponse {
    private int id;
    private String rutina;
    private String descripcion;
    private String imagen;
    private int descanso_seg;
    private List<EjercicioDetalleResponse> ejercicios;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRutina() {
        return this.rutina;
    }

    public void setRutina(String rutina) {
        this.rutina = rutina;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return this.imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getDescanso_seg() {
        return this.descanso_seg;
    }

    public void setDescanso_seg(int descanso_seg) {
        this.descanso_seg = descanso_seg;
    }

    public List<EjercicioDetalleResponse> getEjercicios() {
        return this.ejercicios;
    }

    public void setEjercicios(List<EjercicioDetalleResponse> ejercicios) {
        this.ejercicios = ejercicios;
    }
}
