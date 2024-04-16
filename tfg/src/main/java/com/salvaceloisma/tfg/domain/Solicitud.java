import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Long idSolicitud;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio; 

    @Column(name = "fecha_fin")
    private LocalDate fechaFin; 

    private String horario;

    @Column(name = "numero_convenio")
    private String numeroConvenio;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_solicitud_usuario"))
    private Usuario usuario;

    private boolean estado;

    @Column(name = "datos_alumno")
    private String datosAlumno;
    
    public Solicitud(LocalDate fechaInicio, LocalDate fechaFin, String horario, String numeroConvenio, Usuario usuario, boolean estado, String datosAlumno) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horario = horario;
        this.numeroConvenio = numeroConvenio;
        this.usuario = usuario;
        this.estado = estado;
        this.datosAlumno = datosAlumno;
    }
}
