package com.example.gymweb.model;

<<<<<<< Updated upstream
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
=======
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
>>>>>>> Stashed changes
    private int orden;

    public EjercicioDetalle(int id, RutinaDetalle rutinaDetalle, Ejercicio ejercicio, List<Serie> series, int orden) {
        this.id = id;
        this.rutinaDetalle = rutinaDetalle;
        this.ejercicio = ejercicio;
        this.series = series;
        this.orden = orden;
    }

    public List<Serie> getSeries() {
<<<<<<< Updated upstream
        return series;
=======
        return this.series;
>>>>>>> Stashed changes
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }

    public EjercicioDetalle() {
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

    public RutinaDetalle getRutinaDetalle() {
<<<<<<< Updated upstream
        return rutinaDetalle;
=======
        return this.rutinaDetalle;
>>>>>>> Stashed changes
    }

    public void setRutinaDetalle(RutinaDetalle rutinaDetalle) {
        this.rutinaDetalle = rutinaDetalle;
    }

    public Ejercicio getEjercicio() {
<<<<<<< Updated upstream
        return ejercicio;
=======
        return this.ejercicio;
>>>>>>> Stashed changes
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
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
