package com.example.gymweb.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(
        name = "ejerciciodetalle"
)
public class EjercicioDetalle {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_ejec_det"
    )
    private int id;
    @ManyToOne
    @JoinColumn(
            name = "rutinaDetalle_id"
    )
    private RutinaDetalle rutinaDetalle;
    @ManyToOne
    @JoinColumn(
            name = "ejercicio_id"
    )
    private Ejercicio ejercicio;
    @OneToMany(
            mappedBy = "ejercicioDetalle",
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    private List<Serie> series;
    @Column(
            name = "orden"
    )
    private int orden;

    public EjercicioDetalle(int id, RutinaDetalle rutinaDetalle, Ejercicio ejercicio, List<Serie> series, int orden) {
        this.id = id;
        this.rutinaDetalle = rutinaDetalle;
        this.ejercicio = ejercicio;
        this.series = series;
        this.orden = orden;
    }

    public List<Serie> getSeries() {
        return this.series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }

    public EjercicioDetalle() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RutinaDetalle getRutinaDetalle() {
        return this.rutinaDetalle;
    }

    public void setRutinaDetalle(RutinaDetalle rutinaDetalle) {
        this.rutinaDetalle = rutinaDetalle;
    }

    public Ejercicio getEjercicio() {
        return this.ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    public int getOrden() {
        return this.orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
}
