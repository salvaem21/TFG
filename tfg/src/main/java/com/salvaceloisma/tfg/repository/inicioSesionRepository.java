package com.salvaceloisma.tfg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salvaceloisma.tfg.domain.Usuario;

@Repository
public interface InicioSesionRepository extends JpaRepository<Usuario,Long> {
    public Usuario findByCorreo(String correo);
}
