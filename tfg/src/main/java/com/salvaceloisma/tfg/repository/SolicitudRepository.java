package com.salvaceloisma.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.domain.Usuario;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud,String> {
    List<Solicitud> findByIdSolicitud(String idSolicitud);
    List<Solicitud> findByNumeroConvenio(String numeroConvenio);
    List<Solicitud> findAllByUsuario(Usuario usuario);
}

