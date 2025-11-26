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
        name = "preferencianotificacion"
)
public class PreferenciaNotificacion {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_pref"
    )
    private int id;
    @OneToOne
    @JoinColumn(
            name = "usuario_id"
    )
    private Usuario usuario;
    private Boolean silencioso;

    public PreferenciaNotificacion(int id, Usuario usuario, Boolean silencioso) {
        this.id = id;
        this.usuario = usuario;
        this.silencioso = silencioso;
    }

    public PreferenciaNotificacion() {
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

    public Boolean getSilencioso() {
        return this.silencioso;
    }

    public void setSilencioso(Boolean silencioso) {
        this.silencioso = silencioso;
    }
}

