package com.example.gymweb.dto.Response;

import com.example.gymweb.dto.Request.SerieRequest;

import java.util.List;

public class EjercicioDetalleResponse {

    private int id;
    private String ejercicio;
    private List<SerieResponse> series;

    public EjercicioDetalleResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public List<SerieResponse> getSeries() {
        return series;
    }

    public void setSeries(List<SerieResponse> series) {
        this.series = series;
    }
}
