package com.example.gymweb.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table (name = "plan")
public class Plan {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private int id;

    private String nombre;
    private BigDecimal precio;
    private String periodo;

    public Plan(int id, String nombre, BigDecimal precio, String periodo) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.periodo = periodo;
    }

    public Plan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}
