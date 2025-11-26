package com.example.gymweb.model;

<<<<<<< Updated upstream
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table (name = "pago")
public class Pago {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private int id;

    @ManyToOne
    @JoinColumn (name = "membresia_id")
    private Membresia membresia;

    @Enumerated(EnumType.STRING)
    private EstadoPago estado;

    private LocalDateTime fecha;
    private BigDecimal monto;
=======
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "pago"
)
public class Pago {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_pago"
    )
    private int id;
    @ManyToOne
    @JoinColumn(
            name = "membresia_id"
    )
    private Membresia membresia;
    @Enumerated(EnumType.STRING)
    private EstadoPago estado;
    @Column(
            name = "fecha"
    )
    private LocalDateTime fecha;
    private BigDecimal monto;
    @Column(
            name = "comprobante_url"
    )
>>>>>>> Stashed changes
    private String comprobante_url;

    public Pago(int id, Membresia membresia, EstadoPago estado, LocalDateTime fecha, BigDecimal monto, String comprobante_url) {
        this.id = id;
        this.membresia = membresia;
        this.estado = estado;
        this.fecha = fecha;
        this.monto = monto;
        this.comprobante_url = comprobante_url;
    }

    public Pago() {
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

    public Membresia getMembresia() {
<<<<<<< Updated upstream
        return membresia;
=======
        return this.membresia;
>>>>>>> Stashed changes
    }

    public void setMembresia(Membresia membresia) {
        this.membresia = membresia;
    }

    public EstadoPago getEstado() {
<<<<<<< Updated upstream
        return estado;
=======
        return this.estado;
>>>>>>> Stashed changes
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
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

    public BigDecimal getMonto() {
<<<<<<< Updated upstream
        return monto;
=======
        return this.monto;
>>>>>>> Stashed changes
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getComprobante_url() {
<<<<<<< Updated upstream
        return comprobante_url;
=======
        return this.comprobante_url;
>>>>>>> Stashed changes
    }

    public void setComprobante_url(String comprobante_url) {
        this.comprobante_url = comprobante_url;
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
