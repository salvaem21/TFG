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
    //CLASE MENSAJE CON DATOS COMO REMITENTE, DESTINATARIO, SOLICITUD Y NOVEDAD PARA SABER QUIEN INTERVIENE
    //Y QUIEN TIENE UNA SOLICITUD CONCRETAMENTE (ESTA HECHO PARA LIBERAR UN POCO DE ESPACIO EN LA TABLA SOLICITUD)
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

    public Mensaje(String contenido) {
        this.contenido = contenido;
        this.novedad = true;
    }
}
