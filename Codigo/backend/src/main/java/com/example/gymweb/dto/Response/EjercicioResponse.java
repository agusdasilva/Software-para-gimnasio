package com.example.gymweb.dto.Response;

public class EjercicioResponse {
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
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

    public String getGrupo_muscular() {
<<<<<<< Updated upstream
        return grupo_muscular;
=======
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

    public boolean isEs_global() {
<<<<<<< Updated upstream
        return es_global;
=======
        return this.es_global;
>>>>>>> Stashed changes
    }

    public void setEs_global(boolean es_global) {
        this.es_global = es_global;
    }
}
