package com.salvaceloisma.tfg.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.enumerados.EstadoSolicitud;
import com.salvaceloisma.tfg.repository.SolicitudRepository;

@Service
public class SolicitudService {
    
    @Autowired
    private SolicitudRepository solicitudRepository;

    public List<Solicitud> findAll() {
        return solicitudRepository.findAll();
    }

    public List<Solicitud> findByISolicitudes(String idSolicitud) {
        return solicitudRepository.findByIdSolicitud(idSolicitud);
    }
    public List<Solicitud> findByNumeroConvenio(Integer numeroConvenio) {
        return solicitudRepository.findByNumeroConvenio(numeroConvenio);
    }

    public Solicitud save(String idSolicitud, Integer numeroConvenio,String empresa, String cif, String tutorEmpresa, String direccion, String localidad, String cp, String cicloFormativo, Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin, int horasDia, int horasTotales, String horario, String observaciones,EstadoSolicitud estado) {
        Solicitud solicitud = new Solicitud(idSolicitud, numeroConvenio, empresa, cif, tutorEmpresa, direccion, localidad, cp, cicloFormativo, usuario, fechaInicio, fechaFin, horasDia, horasTotales, horario, observaciones, estado);
        return solicitudRepository.save(solicitud);
    }

    public Solicitud findById(String idSolicitud) {
        return solicitudRepository.findById(idSolicitud).orElse(null); // Utiliza el método orElse(null) para manejar el caso de que no se encuentre ninguna solicitud con el ID especificado
    }
    
    public void update(String idSolicitud, Integer numeroConvenio,String empresa, String cif, String tutorEmpresa, String direccion, String localidad, String cp, String cicloFormativo, Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin, int horasDia, int horasTotales, String horario, String observaciones,EstadoSolicitud estado) {
        // Obtener la solicitud existente por su ID
        Solicitud solicitud = solicitudRepository.findById(idSolicitud).orElse(null);
        if (solicitud != null) {
            // Actualizar los atributos de la solicitud con los valores proporcionados
            solicitud.setNumeroConvenio(numeroConvenio);
            solicitud.setUsuario(usuario);
            // Guardar la solicitud actualizada en la base de datos
            solicitudRepository.save(solicitud);
        } else {
            // Manejar el caso de que la solicitud no se encuentre
            throw new RuntimeException("Solicitud no encontrada con el ID: " + idSolicitud);
        }
    }

    public void delete(String idSolicitud) {
        solicitudRepository.deleteById(idSolicitud); // Utiliza el método deleteById para eliminar una solicitud por su ID
    }
}
