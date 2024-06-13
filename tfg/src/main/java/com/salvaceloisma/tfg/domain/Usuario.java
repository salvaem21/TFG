package com.salvaceloisma.tfg.domain;

import java.util.List;

import com.salvaceloisma.tfg.enumerados.RolUsuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Usuario {
    //CLASE USUARIO CON CORREOS UNICOS Y ROLES PARA TENER DISTINTOS INICIOS DE SESION
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String apellido;

    @Column(unique = true)
    private String correo;

    private String contrasenia;

    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    @Column(name = "ruta_carpeta")
    private String rutaCarpeta;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Solicitud> solicitudes;

    public Usuario(String nombre, String apellido, String correo, String contrasenia, RolUsuario rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }
}
