package com.example.gymweb.model;

<<<<<<< Updated upstream

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "ejercicio")
public class Ejercicio {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio")
    private int id;


    private String nombre;
    @Column(name = "grupo_muscular")
    private String grupoMuscular;
    private String equipamiento;

    @Column(name = "es_global")
    private boolean esGlobal;

    @OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL)
=======
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(
        name = "ejercicio"
)
public class Ejercicio {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_ejercicio"
    )
    private int id;
    private String nombre;
    @Column(
            name = "grupo_muscular"
    )
    private String grupoMuscular;
    private String equipamiento;
    @Column(
            name = "es_global"
    )
    private boolean esGlobal;
    @OneToMany(
            mappedBy = "ejercicio",
            cascade = {CascadeType.ALL}
    )
>>>>>>> Stashed changes
    private List<EjercicioDetalle> ejercicioDetalles;

    public Ejercicio(int id, String nombre, String grupoMuscular, String equipamiento, boolean esGlobal, List<EjercicioDetalle> ejercicioDetalles) {
        this.id = id;
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.equipamiento = equipamiento;
        this.esGlobal = esGlobal;
        this.ejercicioDetalles = ejercicioDetalles;
    }

    public Ejercicio() {
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

    public String getNombre() {
<<<<<<< Updated upstream
        return nombre;
=======
        return this.nombre;
>>>>>>> Stashed changes
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrupoMuscular() {
<<<<<<< Updated upstream
        return grupoMuscular;
=======
        return this.grupoMuscular;
>>>>>>> Stashed changes
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public boolean isEsGlobal() {
<<<<<<< Updated upstream
        return esGlobal;
    }

    public List<EjercicioDetalle> getEjercicioDetalles() {
        return ejercicioDetalles;
=======
        return this.esGlobal;
    }

    public List<EjercicioDetalle> getEjercicioDetalles() {
        return this.ejercicioDetalles;
>>>>>>> Stashed changes
    }

    public void setEjercicioDetalles(List<EjercicioDetalle> ejercicioDetalles) {
        this.ejercicioDetalles = ejercicioDetalles;
    }

    public String getEquipamiento() {
<<<<<<< Updated upstream
        return equipamiento;
=======
        return this.equipamiento;
>>>>>>> Stashed changes
    }

    public void setEquipamiento(String equipamiento) {
        this.equipamiento = equipamiento;
    }

    public boolean getEsGlobal() {
<<<<<<< Updated upstream
        return esGlobal;
=======
        return this.esGlobal;
>>>>>>> Stashed changes
    }

    public void setEsGlobal(boolean esGlobal) {
        this.esGlobal = esGlobal;
    }
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
}
