package com.example.gymweb.dto.Request;

public class SerieRequest {
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
        return this.carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public int getRepeticiones() {
        return this.repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public int getOrden() {
        return this.orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
}
