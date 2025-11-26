package com.example.gymweb.dto.Request;

public class RutinaRequest {
    private String nombre;
    private Integer idCreador;
    private String descripcion;
    private String imagen;
    private Integer descanso_seg;
    private Boolean esGlobal = false;

    public RutinaRequest(String nombre, Integer idCreador, String descripcion, String imagen, Integer descanso_seg, Boolean esGlobal) {
        this.nombre = nombre;
        this.idCreador = idCreador;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.descanso_seg = descanso_seg;
        this.esGlobal = esGlobal;
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

    public Integer getIdCreador() {
<<<<<<< Updated upstream
        return idCreador;
=======
        return this.idCreador;
>>>>>>> Stashed changes
    }

    public void setIdCreador(Integer idCreador) {
        this.idCreador = idCreador;
    }

    public String getDescripcion() {
<<<<<<< Updated upstream
        return descripcion;
=======
        return this.descripcion;
>>>>>>> Stashed changes
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
<<<<<<< Updated upstream
        return imagen;
=======
        return this.imagen;
>>>>>>> Stashed changes
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getDescanso_seg() {
<<<<<<< Updated upstream
        return descanso_seg;
=======
        return this.descanso_seg;
>>>>>>> Stashed changes
    }

    public void setDescanso_seg(Integer descanso_seg) {
        this.descanso_seg = descanso_seg;
    }

    public Boolean getEsGlobal() {
<<<<<<< Updated upstream
        return esGlobal;
=======
        return this.esGlobal;
>>>>>>> Stashed changes
    }

    public void setEsGlobal(Boolean esGlobal) {
        this.esGlobal = esGlobal;
    }
}
