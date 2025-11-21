package com.example.gymweb.dto.Request;

public class EjercicioRequest {

    private String nombre;
    private String grupo_muscular;
    private String equipamiento;
    private boolean es_global;

    public EjercicioRequest() {
    }

    public EjercicioRequest(String nombre, String grupo_muscular, String equipamiento, boolean es_global) {
        this.nombre = nombre;
        this.grupo_muscular = grupo_muscular;
        this.equipamiento = equipamiento;
        this.es_global = es_global;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEs_global() {
        return es_global;
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

    public boolean getEs_global() {
        return es_global;
    }

    public void setEs_global(boolean es_global) {
        this.es_global = es_global;
    }
}
