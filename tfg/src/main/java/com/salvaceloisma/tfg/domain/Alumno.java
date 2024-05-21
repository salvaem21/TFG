package com.salvaceloisma.tfg.domain;



import jakarta.persistence.Entity;
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
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dni;
    private String nombre;
    private String apellido;

    @ManyToOne
    @JoinColumn(name = "idSolicitud")
    private Solicitud solicitud;


    // ==================

    public Alumno(String dni, String nombre, String apellido) {
        this.dni =dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }


}
