package com.example.gymweb.model;


import jakarta.persistence.*;

@Entity
@Table (name = "ejercicio")
public class Ejercicio {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio")
    private int id;

    private String nombre;
    private String grupo_muscular;
    private String equipamiento;
    private String estado_contenido;
    private int creado_por;
    private int es_global;

    public Ejercicio(int id, String nombre, String grupo_muscular, String equipamiento, String estado_contenido, int creado_por, int es_global) {
        this.id = id;
        this.nombre = nombre;
        this.grupo_muscular = grupo_muscular;
        this.equipamiento = equipamiento;
        this.estado_contenido = estado_contenido;
        this.creado_por = creado_por;
        this.es_global = es_global;
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

    public String getGrupo_muscular() {
        return grupo_muscular;
    }

    public void setGrupo_muscular(String grupo_muscular) {
        this.grupo_muscular = grupo_muscular;
    }

    public String getEquipamiento() {
        return equipamiento;
    }

    public void setEquipamiento(String equipamiento) {
        this.equipamiento = equipamiento;
    }

    public String getEstado_contenido() {
        return estado_contenido;
    }

    public void setEstado_contenido(String estado_contenido) {
        this.estado_contenido = estado_contenido;
    }

    public int getCreado_por() {
        return creado_por;
    }

    public void setCreado_por(int creado_por) {
        this.creado_por = creado_por;
    }

    public int getEs_global() {
        return es_global;
    }

    public void setEs_global(int es_global) {
        this.es_global = es_global;
    }
}
