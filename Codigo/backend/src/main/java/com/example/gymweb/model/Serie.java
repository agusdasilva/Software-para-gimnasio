package com.example.gymweb.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "serie"
)
public class Serie {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_serie"
    )
    private int id;
    @ManyToOne
    @JoinColumn(
            name = "ejerciciodetalle_id"
    )
    private EjercicioDetalle ejercicioDetalle;
    private String carga;
    private int repeticiones;
    private int orden;

    public Serie(int id, EjercicioDetalle ejercicioDetalle, String carga, int repeticiones, int orden) {
        this.id = id;
        this.ejercicioDetalle = ejercicioDetalle;
        this.carga = carga;
        this.repeticiones = repeticiones;
        this.orden = orden;
    }

    public Serie() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EjercicioDetalle getEjercicioDetalle() {
        return this.ejercicioDetalle;
    }

    public void setEjercicioDetalle(EjercicioDetalle ejercicioDetalle) {
        this.ejercicioDetalle = ejercicioDetalle;
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


