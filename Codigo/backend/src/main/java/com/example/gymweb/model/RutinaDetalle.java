package com.example.gymweb.model;

import jakarta.persistence.*;

@Entity
@Table (name = "rutinadetalle")
public class RutinaDetalle {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_detalle")
    private int id;

    @ManyToOne
    @JoinColumn (name = "rutina_id")
    private Rutina rutina;

    private String descripcion;
    private String imagen;

    public RutinaDetalle(int id, Rutina rutina, String descripcion, String imagen) {
        this.id = id;
        this.rutina = rutina;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public RutinaDetalle() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rutina getRutina() {
        return rutina;
    }

    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
