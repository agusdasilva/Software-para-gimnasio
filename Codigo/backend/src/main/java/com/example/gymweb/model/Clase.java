package com.example.gymweb.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(
        name = "Clase"
)
public class Clase {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_clase"
    )
    private int id;
    private String titulo;
    private String descripcion;
    private int cupo;
    @ManyToOne
    @JoinColumn(
            name = "creador_id"
    )
    private Usuario creador;
    @OneToMany(
            mappedBy = "clase",
            cascade = {CascadeType.ALL}
    )
    private List<UsuarioXClase> usuarios;
    @ManyToMany
    @JoinTable(
            name = "clasexrutina",
            joinColumns = {@JoinColumn(
                    name = "clase_id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "rutina_id"
            )}
    )
    private List<Rutina> rutinas;

    public Clase(int id, String titulo, String descripcion, int cupo, Usuario creador, List<UsuarioXClase> usuarios, List<Rutina> rutinas) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.cupo = cupo;
        this.creador = creador;
        this.usuarios = usuarios;
        this.rutinas = rutinas;
    }

    public Clase() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<UsuarioXClase> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(List<UsuarioXClase> usuarios) {
        this.usuarios = usuarios;
    }

    public int getCupo() {
        return this.cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public List<Rutina> getRutinas() {
        return this.rutinas;
    }

    public void setRutinas(List<Rutina> rutinas) {
        this.rutinas = rutinas;
    }

    public Usuario getCreador() {
        return this.creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }
}
