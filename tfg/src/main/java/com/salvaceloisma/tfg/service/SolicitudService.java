package com.salvaceloisma.tfg.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.repository.SolicitudRepository;

@Service
public class SolicitudService {
    
    @Autowired
    private SolicitudRepository solicitudRepository;

    public List<Solicitud> findAll() {
        return solicitudRepository.findAll();
    }

    public List<Solicitud> findByNombre(String nombre) {
        return solicitudRepository.findByNombre(nombre);
    }

    public Solicitud save(String nombre, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return solicitudRepository.save(new Solicitud(null, null, nombre, nombre, null, false, nombre));
    }

    public Solicitud findById(Long idSolicitud) {
        return solicitudRepository.findById(idSolicitud).get();
    }
    public void update(Long idSolicitud, String nombre) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud).get();
        solicitud.setNombre(nombre);
        solicitudRepository.save(solicitud);
    }

    public void delete(Long idSolicitud) {
        solicitudRepository.delete(solicitudRepository.getReferenceById(idSolicitud));
    }
}
