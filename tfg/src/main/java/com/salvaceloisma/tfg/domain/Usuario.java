package com.salvaceloisma.tfg.domain;

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
    
    @Column(unique = true)
    private String dni;

    private String rol;

    // ==================

    public Usuario(String nombre, String correo, String contrasenia, String dni, String rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.dni = dni;
        this.rol = rol;
    }



}
