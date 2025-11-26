package com.example.gymweb.model;

<<<<<<< Updated upstream
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

=======
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "mensajeclase"
)
public class MensajeClase {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_mensaje"
    )
    private int id;
    @ManyToOne
    @JoinColumn(
            name = "clase_id"
    )
    private Clase clase;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        return id;
=======
        return this.id;
>>>>>>> Stashed changes
    }

    public void setId(int id) {
        this.id = id;
    }

    public Clase getClase() {
<<<<<<< Updated upstream
        return clase;
=======
        return this.clase;
>>>>>>> Stashed changes
    }

    public void setClase(Clase clase) {
        this.clase = clase;
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

    public String getMensaje() {
<<<<<<< Updated upstream
        return mensaje;
    }
=======
        return this.mensaje;
    }

>>>>>>> Stashed changes
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
