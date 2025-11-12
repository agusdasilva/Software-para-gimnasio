package com.example.gymweb.model;

import jakarta.persistence.*;
import org.w3c.dom.Text;

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

    private Date fecha;
    private Text mensaje;

    public MensajeClase(int id, Clase clase, Date fecha, Text mensaje) {
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Text getMensaje() {
        return mensaje;
    }

    public void setMensaje(Text mensaje) {
        this.mensaje = mensaje;
    }
}
