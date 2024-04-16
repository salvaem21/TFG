package com.salvaceloisma.tfg.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Empresa {




@Id
@Column(unique = true)
private Long idEmpresa; 

    private String nombre;
    private String direccion;
    private String email; 
    private int numeroTelefonico; 

    

    public Empresa(Long idEmpresa, String nombre, String direccion, String email, int numeroTelefonico) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.direccion = direccion;
        this.email = email;
        this.numeroTelefonico = numeroTelefonico;
    }
}
