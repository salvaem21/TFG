package com.salvaceloisma.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salvaceloisma.tfg.domain.Alumno;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    List<Alumno> findBySolicitudIdSolicitud(String idSolicitud);
    List<Alumno> findByDni(String dni);
}
