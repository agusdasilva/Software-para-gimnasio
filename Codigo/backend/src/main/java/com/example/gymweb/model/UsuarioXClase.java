package com.example.gymweb.model;

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

    public UsuarioXClase(int id, Usuario usuario, Clase clase) {
        this.id = id;
        this.usuario = usuario;
        this.clase = clase;
    }

    public UsuarioXClase() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }
}
