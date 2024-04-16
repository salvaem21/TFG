package com.salvaceloisma.tfg.domain;

import java.time.LocalDateTime;

import com.salvaceloisma.tfg.enumerados.TipoDocumento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ForeignKey;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long idDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento;

    @Column(name = "estado_firma")
    private boolean estadoFirma;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_de_firma")
    private LocalDateTime fechaDeFirma;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_documento_usuario"))
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_solicitud", referencedColumnName = "id_solicitud", foreignKey = @ForeignKey(name = "FK_documento_solicitud"))
    private Solicitud solicitud;

    @ManyToOne
    @JoinColumn(name = "id_empresa", referencedColumnName = "idEmpresa", foreignKey = @ForeignKey(name = "FK_documento_empresa"))
    private Empresa empresa;
    
    
    // Constructor

    public Documento(TipoDocumento tipoDocumento, boolean estadoFirma, LocalDateTime fechaCreacion, LocalDateTime fechaDeFirma, Usuario usuario, Solicitud solicitud, Empresa empresa) {
        this.tipoDocumento = tipoDocumento;
        this.estadoFirma = estadoFirma;
        this.fechaCreacion = fechaCreacion;
        this.fechaDeFirma = fechaDeFirma;
        this.usuario = usuario;
        this.solicitud = solicitud;
        this.empresa = empresa;
    }
}
