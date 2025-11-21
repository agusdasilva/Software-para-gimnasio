package com.example.gymweb.model;

import jakarta.persistence.*;

@Entity
@Table (name = "perfilusuario")
public class PerfilUsuario {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private int id;

    @OneToOne
    @JoinColumn (name = "id_usuario")
    private Usuario usuario;

    private String descripcion;
    private String foto_url;
    private String telefono;

    public PerfilUsuario(int id, Usuario usuario, String descripcion, String foto_url, String telefono) {
        this.id = id;
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.foto_url = foto_url;
        this.telefono = telefono;
    }

    public PerfilUsuario() {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
