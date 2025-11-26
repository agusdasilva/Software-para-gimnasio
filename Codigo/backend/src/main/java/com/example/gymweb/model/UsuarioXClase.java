package com.example.gymweb.model;

<<<<<<< Updated upstream
import jakarta.persistence.*;

@Entity
@Table(name = "usuarioxclase")
public class UsuarioXClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usu_clase")
    private int id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "clase_id")
    private Clase clase;

    private boolean aprobado = false; // entrenador aprueba

    public UsuarioXClase() {
    }

    public int getId() {
        return id;
=======
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "usuarioxclase"
)
public class UsuarioXClase {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_usu_clase"
    )
    private int id;
    @ManyToOne
    @JoinColumn(
            name = "usuario_id"
    )
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(
            name = "clase_id"
    )
    private Clase clase;
    private boolean aprobado = false;

    public int getId() {
        return this.id;
>>>>>>> Stashed changes
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
<<<<<<< Updated upstream
        return usuario;
=======
        return this.usuario;
>>>>>>> Stashed changes
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Clase getClase() {
<<<<<<< Updated upstream
        return clase;
=======
        return this.clase;
>>>>>>> Stashed changes
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public boolean isAprobado() {
<<<<<<< Updated upstream
        return aprobado;
=======
        return this.aprobado;
>>>>>>> Stashed changes
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }
}
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
