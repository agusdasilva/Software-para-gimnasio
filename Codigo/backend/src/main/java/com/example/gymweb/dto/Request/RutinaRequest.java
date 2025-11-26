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
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdCreador() {
        return this.idCreador;
    }

    public void setIdCreador(Integer idCreador) {
        this.idCreador = idCreador;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return this.imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getDescanso_seg() {
        return this.descanso_seg;
    }

    public void setDescanso_seg(Integer descanso_seg) {
        this.descanso_seg = descanso_seg;
    }

    public Boolean getEsGlobal() {
        return this.esGlobal;
    }

    public void setEsGlobal(Boolean esGlobal) {
        this.esGlobal = esGlobal;
    }
}
