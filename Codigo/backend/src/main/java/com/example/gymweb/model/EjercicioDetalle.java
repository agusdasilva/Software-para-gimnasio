package com.example.gymweb.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "ejerciciodetalle")
public class EjercicioDetalle {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_ejec_det")
    private int id;

    @ManyToOne
    @JoinColumn (name = "rutinaDetalle_id")
    private RutinaDetalle rutinaDetalle;

    @ManyToOne
    @JoinColumn (name = "ejercicio_id")
    private Ejercicio ejercicio;

    @OneToMany(mappedBy = "ejercicioDetalle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Serie> series;

    @Column(name = "orden")
    private int orden;

    public EjercicioDetalle(int id, RutinaDetalle rutinaDetalle, Ejercicio ejercicio, List<Serie> series, int orden) {
        this.id = id;
        this.rutinaDetalle = rutinaDetalle;
        this.ejercicio = ejercicio;
        this.series = series;
        this.orden = orden;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }

    public EjercicioDetalle() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RutinaDetalle getRutinaDetalle() {
        return rutinaDetalle;
    }

    public void setRutinaDetalle(RutinaDetalle rutinaDetalle) {
        this.rutinaDetalle = rutinaDetalle;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
}
