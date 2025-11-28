package com.example.gymweb.dto.Request;

public class ActualizarRutinaRequest {
    private String nombre;
    private String descripcion;
    private String imagen;
    private Integer descanso_seg;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Integer getDescanso_seg() { return descanso_seg; }
    public void setDescanso_seg(Integer descanso_seg) { this.descanso_seg = descanso_seg; }
}
