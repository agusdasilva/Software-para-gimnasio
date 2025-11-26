package com.example.gymweb.dto.Request;

import java.util.List;

public class EjercicioDetalleRequest {
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        return idEjercicio;
=======
        return this.idEjercicio;
>>>>>>> Stashed changes
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public List<SerieRequest> getSeries() {
<<<<<<< Updated upstream
        return series;
    }

    public int getOrden() {
        return orden;
=======
        return this.series;
    }

    public int getOrden() {
        return this.orden;
>>>>>>> Stashed changes
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getIdRutinaDetalle() {
<<<<<<< Updated upstream
        return idRutinaDetalle;
=======
        return this.idRutinaDetalle;
>>>>>>> Stashed changes
    }

    public void setIdRutinaDetalle(int idRutinaDetalle) {
        this.idRutinaDetalle = idRutinaDetalle;
    }

    public void setSeries(List<SerieRequest> series) {
        this.series = series;
    }
}
