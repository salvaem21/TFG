package com.salvaceloisma.tfg.domain;


import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String correo;

    private String contrasenia;

    private Date fechaNac;

    private String rol;

    // ==================

    public Usuario(String nombre, String correo, String contrasenia, Date fechaNac) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.fechaNac = fechaNac;
        this.rol = "Alumno";
    }



}
