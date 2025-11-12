package com.example.gymweb.model;

import jakarta.persistence.*;

import javax.xml.crypto.Data;
import java.util.Date;

@Entity
@Table (name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_usuario")
    private int id;

    private String nombre;
    private String email;
    private String password_hash;
    private Estado estado;
    private Date fechaAlta;
    private Rol rol;
}
