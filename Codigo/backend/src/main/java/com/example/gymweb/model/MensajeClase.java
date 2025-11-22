package com.example.gymweb.model;

import jakarta.persistence.*;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table (name = "mensajeclase")
public class MensajeClase {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private int id;

    @ManyToOne
    @JoinColumn (name = "clase_id")
    private Clase clase;

    private LocalDateTime fecha;
    private String mensaje;

    public MensajeClase(int id, Clase clase, LocalDateTime fecha, String mensaje) {
        this.id = id;
        this.clase = clase;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    public MensajeClase() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
