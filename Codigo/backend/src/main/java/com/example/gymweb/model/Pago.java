package com.example.gymweb.model;

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
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Membresia getMembresia() {
        return this.membresia;
    }

    public void setMembresia(Membresia membresia) {
        this.membresia = membresia;
    }

    public EstadoPago getEstado() {
        return this.estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMonto() {
        return this.monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getComprobante_url() {
        return this.comprobante_url;
    }

    public void setComprobante_url(String comprobante_url) {
        this.comprobante_url = comprobante_url;
    }
}
