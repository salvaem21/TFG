package com.salvaceloisma.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salvaceloisma.tfg.domain.Usuario;

@Repository
public interface inicioSesionRepository extends JpaRepository<Usuario,Long> {
    public List<Usuario> findByCorreo(String correo);
}
