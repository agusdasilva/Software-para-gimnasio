package com.example.gymweb.dto.Response;


public class SerieResponse {
    private int id;
    private String carga;
    private int repeticiones;
    private int orden;

    public SerieResponse(int id, String carga, int repeticiones, int orden) {
        this.id = id;
        this.carga = carga;
        this.repeticiones = repeticiones;
        this.orden = orden;
    }

    public SerieResponse() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

