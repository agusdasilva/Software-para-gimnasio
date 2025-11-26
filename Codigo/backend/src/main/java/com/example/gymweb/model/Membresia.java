package com.example.gymweb.model;

<<<<<<< Updated upstream
import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table (name = "membresia")
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
import java.time.LocalDateTime;
@Entity
@Table(name = "membresia")
>>>>>>> Stashed changes
public class Membresia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membresia")
    private int id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

<<<<<<< Updated upstream
    private LocalDateTime fecha_inicio;
    private LocalDateTime fecha_fin;
    private Estado estado;

    public Membresia(int id, Usuario usuario, Plan plan, LocalDateTime fecha_inicio, LocalDateTime fecha_fin, Estado estado) {
        this.id = id;
        this.usuario = usuario;
        this.plan = plan;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.estado = estado;
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

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public LocalDateTime getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDateTime fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDateTime getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDateTime fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
=======
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Enumerated(EnumType.ORDINAL)
    private EstadoMembresia estado;

    public Membresia() {}

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Plan getPlan() { return plan; }

    public void setPlan(Plan plan) { this.plan = plan; }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoMembresia getEstado() { return estado; }

    public void setEstado(EstadoMembresia estado) { this.estado = estado; }
>>>>>>> Stashed changes
}
