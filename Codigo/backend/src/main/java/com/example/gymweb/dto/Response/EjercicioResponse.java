package com.example.gymweb.dto.Response;

public class EjercicioResponse {
    private int id;
    private String nombre;
    private String grupo_muscular;
    private String equipamiento;
    private boolean es_global;

    public EjercicioResponse(int id, String nombre, String grupo_muscular, String equipamiento, boolean es_global) {
        this.id = id;
        this.nombre = nombre;
        this.grupo_muscular = grupo_muscular;
        this.equipamiento = equipamiento;
        this.es_global = es_global;
    }

    public EjercicioResponse() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrupo_muscular() {
        return this.grupo_muscular;
    }

    public void setGrupo_muscular(String grupo_muscular) {
        this.grupo_muscular = grupo_muscular;
    }

    public String getEquipamiento() {
        return this.equipamiento;
    }

    public void setEquipamiento(String equipamiento) {
        this.equipamiento = equipamiento;
    }

    public boolean isEs_global() {
        return this.es_global;
    }

    public void setEs_global(boolean es_global) {
        this.es_global = es_global;
    }
}
