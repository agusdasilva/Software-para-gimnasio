package com.example.gymweb.model;

<<<<<<< Updated upstream
import jakarta.persistence.*;

import javax.xml.crypto.Data;
=======
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
>>>>>>> Stashed changes
import java.util.Date;
import java.util.List;

@Entity
<<<<<<< Updated upstream
@Table (name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_usuario")
    private int id;

    @OneToMany(mappedBy = "creador")
    private List<Rutina> rutinas;

=======
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
>>>>>>> Stashed changes
    private String nombre;
    private String email;
    private String password_hash;
    private Estado estado;
    private Date fechaAlta;
    private Rol rol;

<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        return id;
=======
        return this.id;
>>>>>>> Stashed changes
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Rutina> getRutinas() {
<<<<<<< Updated upstream
        return rutinas;
=======
        return this.rutinas;
>>>>>>> Stashed changes
    }

    public void setRutinas(List<Rutina> rutinas) {
        this.rutinas = rutinas;
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

    public String getEmail() {
<<<<<<< Updated upstream
        return email;
=======
        return this.email;
>>>>>>> Stashed changes
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
<<<<<<< Updated upstream
        return password_hash;
=======
        return this.password_hash;
>>>>>>> Stashed changes
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public Estado getEstado() {
<<<<<<< Updated upstream
        return estado;
=======
        return this.estado;
>>>>>>> Stashed changes
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Date getFechaAlta() {
<<<<<<< Updated upstream
        return fechaAlta;
=======
        return this.fechaAlta;
>>>>>>> Stashed changes
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Rol getRol() {
<<<<<<< Updated upstream
        return rol;
=======
        return this.rol;
>>>>>>> Stashed changes
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
