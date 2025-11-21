package com.example.gymweb.dto.Request;

import java.util.List;

public class EjercicioDetalleRequest {

    private int idEjercicio;
    private List<SerieRequest> series;
    private int orden;
    private int idRutinaDetalle;

    public EjercicioDetalleRequest(int idEjercicio, List<SerieRequest> series) {
        this.idEjercicio = idEjercicio;
        this.series = series;
    }

    public EjercicioDetalleRequest() {
    }

    public int getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public List<SerieRequest> getSeries() {
        return series;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getIdRutinaDetalle() {
        return idRutinaDetalle;
    }

    public void setIdRutinaDetalle(int idRutinaDetalle) {
        this.idRutinaDetalle = idRutinaDetalle;
    }

    public void setSeries(List<SerieRequest> series) {
        this.series = series;
    }
}
