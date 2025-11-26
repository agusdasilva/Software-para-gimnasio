package com.example.gymweb.dto.Request;

public class SerieRequest {
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
    private String carga;
    private int repeticiones;
    private int orden;

    public SerieRequest(String carga, int repeticiones, int orden) {
        this.carga = carga;
        this.repeticiones = repeticiones;
        this.orden = orden;
    }

    public SerieRequest() {
    }

    public String getCarga() {
<<<<<<< Updated upstream
        return carga;
=======
        return this.carga;
>>>>>>> Stashed changes
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public int getRepeticiones() {
<<<<<<< Updated upstream
        return repeticiones;
=======
        return this.repeticiones;
>>>>>>> Stashed changes
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public int getOrden() {
<<<<<<< Updated upstream
        return orden;
=======
        return this.orden;
>>>>>>> Stashed changes
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
}
