package com.example.gymweb.model;


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
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public boolean isEsGlobal() {
        return esGlobal;
    }

    public List<EjercicioDetalle> getEjercicioDetalles() {
        return ejercicioDetalles;
    }

    public void setEjercicioDetalles(List<EjercicioDetalle> ejercicioDetalles) {
        this.ejercicioDetalles = ejercicioDetalles;
    }

    public String getEquipamiento() {
        return equipamiento;
    }

    public void setEquipamiento(String equipamiento) {
        this.equipamiento = equipamiento;
    }

    public boolean getEsGlobal() {
        return esGlobal;
    }

    public void setEsGlobal(boolean esGlobal) {
        this.esGlobal = esGlobal;
    }

}
