package com.example.gymweb.model;

<<<<<<< Updated upstream
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table (name = "plan")
public class Plan {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private int id;

=======
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(
        name = "plan"
)
public class Plan {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_plan"
    )
    private int id;
>>>>>>> Stashed changes
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

    public BigDecimal getPrecio() {
<<<<<<< Updated upstream
        return precio;
=======
        return this.precio;
>>>>>>> Stashed changes
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getPeriodo() {
<<<<<<< Updated upstream
        return periodo;
=======
        return this.periodo;
>>>>>>> Stashed changes
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
