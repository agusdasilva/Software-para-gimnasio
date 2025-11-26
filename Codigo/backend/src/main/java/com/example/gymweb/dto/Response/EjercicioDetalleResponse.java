package com.example.gymweb.dto.Response;

import java.util.List;

public class EjercicioDetalleResponse {
    private int id;
    private String ejercicio;
    private List<SerieResponse> series;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEjercicio() {
        return this.ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public List<SerieResponse> getSeries() {
        return this.series;
    }

    public void setSeries(List<SerieResponse> series) {
        this.series = series;
    }
}
