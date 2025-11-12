package com.example.gymweb.model;

import jakarta.persistence.*;

@Entity
@Table (name = "preferencianotificacion")
public class PrefereciaNotificacion {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_pref")
    private int id;

    @OneToOne
    @JoinColumn (name = "usuario_id")
    private Usuario usuario;
    private int silencioso;

    public PrefereciaNotificacion(int id, Usuario usuario, int silencioso) {
        this.id = id;
        this.usuario = usuario;
        this.silencioso = silencioso;
    }

    public PrefereciaNotificacion() {
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

    public int getSilencioso() {
        return silencioso;
    }

    public void setSilencioso(int silencioso) {
        this.silencioso = silencioso;
    }
}

