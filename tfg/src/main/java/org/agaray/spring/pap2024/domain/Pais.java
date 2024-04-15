package org.agaray.spring.pap2024.domain;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    @JsonIgnore
    @OneToMany(mappedBy = "nace")
    private Collection<Persona> nacidos;

    @JsonIgnore
    @OneToMany(mappedBy = "vive")
    private Collection<Persona> residentes;

    // ==================
    public Pais() {
        this.nacidos = new ArrayList<>();
        this.nacidos = new ArrayList<>();
        this.residentes = new ArrayList<>();
    }

    public Pais(String nombre) {
        this.nombre = nombre;
        this.nacidos = new ArrayList<>();
        this.nacidos = new ArrayList<>();

    }

}
