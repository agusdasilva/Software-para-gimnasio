package com.example.gymweb.model;

<<<<<<< Updated upstream
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

=======
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
            name = "id_usuario"
    )
    private Usuario usuario;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        return id;
=======
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

    public String getFoto_url() {
<<<<<<< Updated upstream
        return foto_url;
=======
        return this.foto_url;
>>>>>>> Stashed changes
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

    public String getTelefono() {
<<<<<<< Updated upstream
        return telefono;
=======
        return this.telefono;
>>>>>>> Stashed changes
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
