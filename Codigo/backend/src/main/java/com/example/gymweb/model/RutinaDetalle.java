package com.example.gymweb.model;

<<<<<<< Updated upstream
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
=======

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(
        name = "rutinadetalle"
)
public class RutinaDetalle {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_detalle"
    )
    private int id;
    @OneToOne
    @JoinColumn(
            name = "rutina_id",
            unique = true
    )
    private Rutina rutina;
    @OneToMany(
            mappedBy = "rutinaDetalle",
            cascade = {CascadeType.ALL}
    )
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        return ejercicios;
=======
        return this.ejercicios;
>>>>>>> Stashed changes
    }

    public void setEjercicios(List<EjercicioDetalle> ejercicios) {
        this.ejercicios = ejercicios;
    }

    public int getDescanso_seg() {
<<<<<<< Updated upstream
        return descanso_seg;
=======
        return this.descanso_seg;
>>>>>>> Stashed changes
    }

    public void setDescanso_seg(int descanso_seg) {
        this.descanso_seg = descanso_seg;
    }

    public RutinaDetalle() {
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

    public Rutina getRutina() {
<<<<<<< Updated upstream
        return rutina;
=======
        return this.rutina;
>>>>>>> Stashed changes
    }

    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
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
}
