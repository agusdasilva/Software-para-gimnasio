package com.example.gymweb.model;

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
        name = "perfilusuario"
)
public class PerfilUsuario {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_perfil"
    )
    private int id;
    @OneToOne
    @JoinColumn(
            name = "usuario_id",
            referencedColumnName = "id_usuario"
    )
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
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto_url() {
        return this.foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
