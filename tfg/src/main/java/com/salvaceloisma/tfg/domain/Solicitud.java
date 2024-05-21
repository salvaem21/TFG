package com.salvaceloisma.tfg.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.salvaceloisma.tfg.enumerados.EstadoSolicitud;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    private String numeroConvenio;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_solicitud_usuario"))
    private Usuario usuario;

    @OneToMany(mappedBy = "solicitud")
    private List<Alumno> alumnos;

    @Column(name = "empresa")
    private String empresa;

    @Column(name = "cif")
    private String cif;

    @Column(name = "tutor_empresa")
    private String tutorEmpresa;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "localidad")
    private String localidad;

    @Column(name = "cp")
    private String cp;

    @Column(name = "ciclo_formativo")
    private String cicloFormativo;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "horas_dia")
    private int horasDia;

    @Column(name = "horas_totales")
    private int horasTotales;

    @Column(name = "horario")
    private String horario;

    @Column(name = "observaciones")
    private String observaciones;

    // @Column(name = "ruta_pdf")
    // private String rutaPDF;

    @Column(name = "ruta_solicitud")
    private String rutaSolicitud;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    public Solicitud(String numeroConvenio, String empresa, String cif, String tutorEmpresa, String direccion,
            String localidad, String cp, String cicloFormativo,
            Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin, int horasDia, int horasTotales, String horario,
            String observaciones, EstadoSolicitud estado) {
        this.idSolicitud = LocalDateTime.now().toString().replaceAll("[^0-9]", "");
        this.numeroConvenio = numeroConvenio;
        this.empresa = empresa;
        this.cif = cif;
        this.tutorEmpresa = tutorEmpresa;
        this.direccion = direccion;
        this.localidad = localidad;
        this.cp = cp;
        this.cicloFormativo = cicloFormativo;
        this.usuario = usuario;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horasDia = horasDia;
        this.horasTotales = horasTotales;
        this.horario = horario;
        this.observaciones = observaciones;
        this.estado = estado;
        this.alumnos = new ArrayList<>(); // Inicializar la lista de alumnos
        this.rutaSolicitud = "";
    }
}
