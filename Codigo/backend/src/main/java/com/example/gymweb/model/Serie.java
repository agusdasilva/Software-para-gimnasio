package com.example.gymweb.model;

<<<<<<< Updated upstream
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

=======

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
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        return id;
=======
        return this.id;
>>>>>>> Stashed changes
    }

    public void setId(int id) {
        this.id = id;
    }

    public EjercicioDetalle getEjercicioDetalle() {
<<<<<<< Updated upstream
        return ejercicioDetalle;
=======
        return this.ejercicioDetalle;
>>>>>>> Stashed changes
    }

    public void setEjercicioDetalle(EjercicioDetalle ejercicioDetalle) {
        this.ejercicioDetalle = ejercicioDetalle;
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


