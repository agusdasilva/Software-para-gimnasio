package com.example.gymweb.model;

<<<<<<< Updated upstream

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Clase")
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clase")
    private int id;

    private String titulo;
    private String descripcion;
    private int cupo;

    @ManyToOne
    @JoinColumn(name = "creador_id")
    private Usuario creador;

    // UsuarioXClase maneja alumnos + entrenador
    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL)
    private List<UsuarioXClase> usuarios;

    // Rutinas adjuntadas a la clase
    @ManyToMany
    @JoinTable(
            name = "clasexrutina",
            joinColumns = @JoinColumn(name = "clase_id"),
            inverseJoinColumns = @JoinColumn(name = "rutina_id")
=======
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(
        name = "Clase"
)
public class Clase {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_clase"
    )
    private int id;
    private String titulo;
    private String descripcion;
    private int cupo;
    @ManyToOne
    @JoinColumn(
            name = "creador_id"
    )
    private Usuario creador;
    @OneToMany(
            mappedBy = "clase",
            cascade = {CascadeType.ALL}
    )
    private List<UsuarioXClase> usuarios;
    @ManyToMany
    @JoinTable(
            name = "clasexrutina",
            joinColumns = {@JoinColumn(
                    name = "clase_id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "rutina_id"
            )}
>>>>>>> Stashed changes
    )
    private List<Rutina> rutinas;

    public Clase(int id, String titulo, String descripcion, int cupo, Usuario creador, List<UsuarioXClase> usuarios, List<Rutina> rutinas) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.cupo = cupo;
        this.creador = creador;
        this.usuarios = usuarios;
        this.rutinas = rutinas;
    }

    public Clase() {
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

    public String getTitulo() {
<<<<<<< Updated upstream
        return titulo;
=======
        return this.titulo;
>>>>>>> Stashed changes
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public List<UsuarioXClase> getUsuarios() {
<<<<<<< Updated upstream
        return usuarios;
=======
        return this.usuarios;
>>>>>>> Stashed changes
    }

    public void setUsuarios(List<UsuarioXClase> usuarios) {
        this.usuarios = usuarios;
    }

    public int getCupo() {
<<<<<<< Updated upstream
        return cupo;
=======
        return this.cupo;
>>>>>>> Stashed changes
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

<<<<<<< Updated upstream

    public List<Rutina> getRutinas() {
        return rutinas;
=======
    public List<Rutina> getRutinas() {
        return this.rutinas;
>>>>>>> Stashed changes
    }

    public void setRutinas(List<Rutina> rutinas) {
        this.rutinas = rutinas;
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
