package com.example.gymweb.model;

import jakarta.persistence.*;

@Entity
@Table (name = "rutina")
public class Rutina {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina")
    private int id;

    private String nombre;

    @OneToOne(mappedBy = "rutina", cascade = CascadeType.ALL)
    private RutinaDetalle rutinaDetalle;

    @OneToOne
    @JoinColumn (name = "creador_id")
    private Usuario creador;

    public Rutina(int id, String nombre, Usuario creador) {
        this.id = id;
        this.nombre = nombre;
        this.creador = creador;
    }

    public Rutina() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RutinaDetalle getRutinaDetalle() {
        return rutinaDetalle;
    }

    public void setRutinaDetalle(RutinaDetalle rutinaDetalle) {
        this.rutinaDetalle = rutinaDetalle;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }
}
