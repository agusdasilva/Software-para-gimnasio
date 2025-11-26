package com.example.gymweb.model;

<<<<<<< Updated upstream
import jakarta.persistence.*;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table (name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private int id;

    @ManyToOne
    @JoinColumn (name = "usuario_id")
=======
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "notificacion"
)
public class Notificacion {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_notificacion"
    )
    private int id;
    @ManyToOne
    @JoinColumn(
            name = "usuario_id"
    )
>>>>>>> Stashed changes
    private Usuario usuario;
    @Lob
    private String mensaje;
    private LocalDateTime fecha;
<<<<<<< Updated upstream
    @Column(name = "leida")
=======
    @Column(
            name = "leida"
    )
>>>>>>> Stashed changes
    private boolean leida;

    public Notificacion(int id, Usuario usuario, String mensaje, LocalDateTime fecha, boolean leida) {
        this.id = id;
        this.usuario = usuario;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.leida = leida;
    }

    public Notificacion() {
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

    public String getMensaje() {
<<<<<<< Updated upstream
        return mensaje;
=======
        return this.mensaje;
>>>>>>> Stashed changes
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha() {
<<<<<<< Updated upstream
        return fecha;
=======
        return this.fecha;
>>>>>>> Stashed changes
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public boolean isLeida() {
<<<<<<< Updated upstream
        return leida;
=======
        return this.leida;
>>>>>>> Stashed changes
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }
}
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
