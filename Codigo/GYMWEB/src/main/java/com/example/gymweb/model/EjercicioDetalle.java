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

    private int descanso_seg;

    public EjercicioDetalle(int id, RutinaDetalle rutinaDetalle, Ejercicio ejercicio, List<Serie> series, int descanso_seg) {
        this.id = id;
        this.rutinaDetalle = rutinaDetalle;
        this.ejercicio = ejercicio;
        this.series = series;
        this.descanso_seg = descanso_seg;
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

    public int getDescanso_seg() {
        return descanso_seg;
    }

    public void setDescanso_seg(int descanso_seg) {
        this.descanso_seg = descanso_seg;
    }
}
