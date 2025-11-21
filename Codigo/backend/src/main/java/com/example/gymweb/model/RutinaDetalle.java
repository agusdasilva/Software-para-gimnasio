package com.example.gymweb.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "rutinadetalle")
public class RutinaDetalle {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_detalle")
    private int id;

    @OneToOne
    @JoinColumn(name = "rutina_id", unique = true)
    private Rutina rutina;

    @OneToMany(mappedBy = "rutinaDetalle", cascade = CascadeType.ALL)
    private List<EjercicioDetalle> ejercicios;
    private String descripcion;
    private String imagen;
    private int descanso_seg;

    public RutinaDetalle(int id, Rutina rutina, List<EjercicioDetalle> ejercicios, String descripcion, String imagen, int descanso_seg) {
        this.id = id;
        this.rutina = rutina;
        this.ejercicios = ejercicios;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.descanso_seg = descanso_seg;
    }

    public List<EjercicioDetalle> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<EjercicioDetalle> ejercicios) {
        this.ejercicios = ejercicios;
    }

    public int getDescanso_seg() {
        return descanso_seg;
    }

    public void setDescanso_seg(int descanso_seg) {
        this.descanso_seg = descanso_seg;
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
