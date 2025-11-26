package com.example.gymweb.model;

<<<<<<< Updated upstream
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
=======
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "rutina"
)
public class Rutina {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_rutina"
    )
    private int id;
    private String nombre;
    @OneToOne(
            mappedBy = "rutina",
            cascade = {CascadeType.ALL}
    )
    private RutinaDetalle rutinaDetalle;
    @OneToOne
    @JoinColumn(
            name = "creador_id"
    )
>>>>>>> Stashed changes
    private Usuario creador;

    public Rutina(int id, String nombre, Usuario creador) {
        this.id = id;
        this.nombre = nombre;
        this.creador = creador;
    }

    public Rutina() {
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

    public RutinaDetalle getRutinaDetalle() {
<<<<<<< Updated upstream
        return rutinaDetalle;
=======
        return this.rutinaDetalle;
>>>>>>> Stashed changes
    }

    public void setRutinaDetalle(RutinaDetalle rutinaDetalle) {
        this.rutinaDetalle = rutinaDetalle;
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

    public Usuario getCreador() {
<<<<<<< Updated upstream
        return creador;
=======
        return this.creador;
>>>>>>> Stashed changes
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }
}
