package org.agaray.spring.pap2024.domain;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data

public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String dni;

    private String nombre;

    @ManyToOne
    private Pais nace;

    @ManyToOne
    private Pais vive;

    @ManyToMany
    private Collection<Aficion> gustos;

    @ManyToMany
    private Collection<Aficion> odios;

    // ==================

    public Persona() {
        this.gustos = new ArrayList<>();
        this.odios = new ArrayList<>();
    }

    public Persona(String dni,String nombre) {
        this.dni = dni;
        this.nombre = nombre;
        this.gustos = new ArrayList<>();
        this.odios = new ArrayList<>();
    }

}
