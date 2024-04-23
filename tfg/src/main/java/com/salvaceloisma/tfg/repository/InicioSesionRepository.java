package com.salvaceloisma.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.enumerados.RolUsuario;

@Repository
public interface InicioSesionRepository extends JpaRepository<Usuario,Long> {
    public Usuario findByCorreo(String correo);
    List<Usuario> findByRol(RolUsuario rol);
}
