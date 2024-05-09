package com.salvaceloisma.tfg.domain;

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
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario remitente;

    @ManyToOne
    private Usuario destinatario;

    @ManyToOne
    private Solicitud solicitud;

    private boolean novedad;

    @Column(columnDefinition = "TEXT")
    private String contenido;
    
    public Mensaje(String contenido){
        this.contenido = contenido;
        this.novedad = true;
    }
}
