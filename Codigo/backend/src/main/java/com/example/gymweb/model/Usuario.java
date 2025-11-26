package com.example.gymweb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "usuario"
)
public class Usuario {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_usuario"
    )
    private int id;
    @OneToMany(
            mappedBy = "creador"
    )
    private List<Rutina> rutinas;
    private String nombre;
    private String email;
    private String password_hash;
    private Estado estado;
    private Date fechaAlta;
    private Rol rol;

    public Usuario(int id, List<Rutina> rutinas, String nombre, String email, String password_hash, Estado estado, Date fechaAlta, Rol rol) {
        this.id = id;
        this.rutinas = rutinas;
        this.nombre = nombre;
        this.email = email;
        this.password_hash = password_hash;
        this.estado = estado;
        this.fechaAlta = fechaAlta;
        this.rol = rol;
    }

    public Usuario() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Rutina> getRutinas() {
        return this.rutinas;
    }

    public void setRutinas(List<Rutina> rutinas) {
        this.rutinas = rutinas;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return this.password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Date getFechaAlta() {
        return this.fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Rol getRol() {
        return this.rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
