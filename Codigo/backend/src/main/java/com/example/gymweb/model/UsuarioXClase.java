package com.example.gymweb.model;

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

    public Clase getClase() {
        return this.clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public boolean isAprobado() {
        return this.aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }
}

