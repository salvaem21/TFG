package com.salvaceloisma.tfg.domain;


import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String dni;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;

    @ManyToOne(optional = true)
    private Solicitud idSolicitud;


    // ==================

    public Alumno(String dni, String nombre, String apellido, LocalDate fechaNacimiento) {
        this.dni =dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }


}
