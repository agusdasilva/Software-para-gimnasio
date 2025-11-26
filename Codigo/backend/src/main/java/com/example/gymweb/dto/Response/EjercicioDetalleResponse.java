package com.example.gymweb.dto.Response;

<<<<<<< Updated upstream
import com.example.gymweb.dto.Request.SerieRequest;
=======
>>>>>>> Stashed changes

import java.util.List;

public class EjercicioDetalleResponse {
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
    private int id;
    private String ejercicio;
    private List<SerieResponse> series;

<<<<<<< Updated upstream
    public EjercicioDetalleResponse() {
    }

    public int getId() {
        return id;
=======
    public int getId() {
        return this.id;
>>>>>>> Stashed changes
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEjercicio() {
<<<<<<< Updated upstream
        return ejercicio;
=======
        return this.ejercicio;
>>>>>>> Stashed changes
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public List<SerieResponse> getSeries() {
<<<<<<< Updated upstream
        return series;
=======
        return this.series;
>>>>>>> Stashed changes
    }

    public void setSeries(List<SerieResponse> series) {
        this.series = series;
    }
}
