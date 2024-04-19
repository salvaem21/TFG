package com.salvaceloisma.tfg.domain;


import java.time.LocalDateTime;

import com.salvaceloisma.tfg.enumerados.EstadoSolicitud;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Solicitud {

    @Id
    @Column(name = "id_solicitud")
    private String idSolicitud;

    @Column(name = "numero_convenio")
    private Integer numeroConvenio;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_solicitud_usuario"))
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;
    
    public Solicitud(Integer numeroConvenio, Usuario usuario, EstadoSolicitud estado) {
        this.idSolicitud = LocalDateTime.now().toString().replaceAll("[^0-9]", "");
        this.numeroConvenio = numeroConvenio;
        this.usuario = usuario;
        this.estado = estado;
    }
}
