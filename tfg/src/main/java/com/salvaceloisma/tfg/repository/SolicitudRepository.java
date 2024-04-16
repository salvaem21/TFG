package com.salvaceloisma.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salvaceloisma.tfg.domain.Solicitud;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud,Long> {
    public List<Solicitud> findByNombre(String nombre);
}
