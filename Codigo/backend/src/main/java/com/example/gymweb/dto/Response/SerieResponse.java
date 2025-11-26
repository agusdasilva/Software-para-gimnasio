package com.example.gymweb.dto.Response;

<<<<<<< Updated upstream
public class SerieResponse {

=======

public class SerieResponse {
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        return id;
=======
        return this.id;
>>>>>>> Stashed changes
    }

    public void setId(int id) {
        this.id = id;
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
