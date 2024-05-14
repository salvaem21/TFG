package com.salvaceloisma.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salvaceloisma.tfg.domain.Mensaje;
import com.salvaceloisma.tfg.domain.Usuario;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByDestinatario(Usuario destinatario);

    Mensaje findBySolicitudIdSolicitud(String idSolicitud);
}
