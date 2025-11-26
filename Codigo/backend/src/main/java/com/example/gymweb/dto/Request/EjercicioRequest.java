package com.example.gymweb.dto.Request;

<<<<<<< Updated upstream
public class EjercicioRequest {

=======

public class EjercicioRequest {
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        return nombre;
=======
        return this.nombre;
>>>>>>> Stashed changes
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEs_global() {
<<<<<<< Updated upstream
        return es_global;
    }

    public String getGrupo_muscular() {
        return grupo_muscular;
=======
        return this.es_global;
    }

    public String getGrupo_muscular() {
        return this.grupo_muscular;
>>>>>>> Stashed changes
    }

    public void setGrupo_muscular(String grupo_muscular) {
        this.grupo_muscular = grupo_muscular;
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

    public boolean getEs_global() {
<<<<<<< Updated upstream
        return es_global;
=======
        return this.es_global;
>>>>>>> Stashed changes
    }

    public void setEs_global(boolean es_global) {
        this.es_global = es_global;
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
