package com.example.gymweb.model;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Clase")
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_clase")
    private int id;

    private String titulo;
    private String descripcion;

    @OneToMany(mappedBy = "clase")
    private List<UsuarioXClase> usuarios;

    public Clase(int id, String titulo, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Clase() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<UsuarioXClase> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioXClase> usuarios) {
        this.usuarios = usuarios;
    }
}
