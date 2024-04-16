package com.salvaceloisma.tfg.service;

import java.time.LocalDate; 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.repository.SolicitudRepository;

@Service
public class SolicitudService {
    
    @Autowired
    private SolicitudRepository solicitudRepository;

    public List<Solicitud> findAll() {
        return solicitudRepository.findAll();
    }

    public List<Solicitud> findByISolicitudes(Long idSolicitud) {
        return solicitudRepository.findByIdSolicitud(idSolicitud);
    }

    public Solicitud save(LocalDate fechaInicio, LocalDate fechaFin, String horario, String numeroConvenio, Usuario usuario, boolean estado, String datosAlumno) {
        // Crear una nueva instancia de Solicitud con los parámetros proporcionados
        Solicitud solicitud = new Solicitud(fechaInicio, fechaFin, horario, numeroConvenio, usuario, estado, datosAlumno);
        return solicitudRepository.save(solicitud);
    }

    public Solicitud findById(Long idSolicitud) {
        return solicitudRepository.findById(idSolicitud).orElse(null); // Utiliza el método orElse(null) para manejar el caso de que no se encuentre ninguna solicitud con el ID especificado
    }
    
    public void update(Long idSolicitud, LocalDate fechaInicio, LocalDate fechaFin, String horario, String numeroConvenio, Usuario usuario, boolean estado, String datosAlumno) {
        // Obtener la solicitud existente por su ID
        Solicitud solicitud = solicitudRepository.findById(idSolicitud).orElse(null);
        if (solicitud != null) {
            // Actualizar los atributos de la solicitud con los valores proporcionados
            solicitud.setFechaInicio(fechaInicio);
            solicitud.setFechaFin(fechaFin);
            solicitud.setHorario(horario);
            solicitud.setNumeroConvenio(numeroConvenio);
            solicitud.setUsuario(usuario);
            solicitud.setEstado(estado);
            solicitud.setDatosAlumno(datosAlumno);
            // Guardar la solicitud actualizada en la base de datos
            solicitudRepository.save(solicitud);
        }
    }

    public void delete(Long idSolicitud) {
        solicitudRepository.deleteById(idSolicitud); // Utiliza el método deleteById para eliminar una solicitud por su ID
    }
}
