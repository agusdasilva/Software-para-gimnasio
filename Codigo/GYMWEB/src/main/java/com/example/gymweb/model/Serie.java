package com.example.gymweb.model;

import jakarta.persistence.*;

@Entity
@Table (name = "serie")
public class Serie {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_serie")
    private int id;

    @ManyToOne
    @JoinColumn (name = "ejerciciodetalle_id")
    private EjercicioDetalle ejercicioDetalle;

    private String carga;
    private int repeticiones;

    public Serie(int id, EjercicioDetalle ejercicioDetalle, String carga, int repeticiones) {
        this.id = id;
        this.ejercicioDetalle = ejercicioDetalle;
        this.carga = carga;
        this.repeticiones = repeticiones;
    }

    public Serie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EjercicioDetalle getEjercicioDetalle() {
        return ejercicioDetalle;
    }

    public void setEjercicioDetalle(EjercicioDetalle ejercicioDetalle) {
        this.ejercicioDetalle = ejercicioDetalle;
    }

    public String getCarga() {
        return carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }
}


