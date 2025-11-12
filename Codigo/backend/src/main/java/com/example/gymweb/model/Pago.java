package com.example.gymweb.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
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

    private Estado estado;
    private Date fecha;
    private BigDecimal monto;
    private String comprobante_url;

    public Pago(int id, Membresia membresia, Estado estado, Date fecha, BigDecimal monto, String comprobante_url) {
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
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Membresia getMembresia() {
        return membresia;
    }

    public void setMembresia(Membresia membresia) {
        this.membresia = membresia;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getComprobante_url() {
        return comprobante_url;
    }

    public void setComprobante_url(String comprobante_url) {
        this.comprobante_url = comprobante_url;
    }
}
