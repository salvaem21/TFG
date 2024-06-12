package com.salvaceloisma.tfg.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.enumerados.EstadoSolicitud;
import com.salvaceloisma.tfg.enumerados.Grados;
import com.salvaceloisma.tfg.enumerados.RolUsuario;
import com.salvaceloisma.tfg.repository.SolicitudRepository;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    public List<Solicitud> findAll() {
        return solicitudRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Solicitud> findAll(Sort sort) {
        return solicitudRepository.findAll(sort);
    }

    public List<Solicitud> findByIdSolicitudes(String idSolicitud) {
        return solicitudRepository.findByIdSolicitud(idSolicitud);
    }

    public List<Solicitud> findByNumeroConvenio(String numeroConvenio) {
        return solicitudRepository.findByNumeroConvenio(numeroConvenio);
    }

    public Solicitud save(String numeroConvenio, String empresa, String cif, String tutorEmpresa, String direccion,
            String localidad, String cp, Grados cicloFormativo, Usuario usuario, LocalDate fechaInicio,
            LocalDate fechaFin, int horasDia, int horasTotales, String horario, String observaciones,
            EstadoSolicitud estado) {
        Solicitud solicitud = new Solicitud(numeroConvenio, empresa, cif, tutorEmpresa, direccion, localidad, cp,
                cicloFormativo, usuario, fechaInicio, fechaFin, horasDia, horasTotales, horario, observaciones, estado);
        return solicitudRepository.save(solicitud);
    }

    public Solicitud save(String numeroConvenio, String empresa, String cif, String tutorEmpresa, String direccion,
            String localidad, String cp, Grados cicloFormativo, Usuario usuario, LocalDate fechaInicio,
            LocalDate fechaFin, int horasDia, int horasTotales, String horario, String observaciones,
            EstadoSolicitud estado, Usuario usuarioSolicitante) throws Exception {
        if (usuarioSolicitante.getRol() != RolUsuario.PROFESOR) {
            throw new Exception("Solo los profesores pueden crear solicitudes");
        }
        Solicitud solicitud = new Solicitud(numeroConvenio, empresa, cif, tutorEmpresa, direccion, localidad, cp,
                cicloFormativo, usuario, fechaInicio, fechaFin, horasDia, horasTotales, horario, observaciones, estado);
        return solicitudRepository.save(solicitud);
    }

    public Solicitud findById(String idSolicitud) {
        return solicitudRepository.findById(idSolicitud).orElse(null);
    }

    public void update(String idSolicitud, String numeroConvenio, String empresa, String cif, String tutorEmpresa,
            String direccion, String localidad, String cp, Grados cicloFormativo, Usuario usuario,
            LocalDate fechaInicio, LocalDate fechaFin, int horasDia, int horasTotales, String horario,
            String observaciones, EstadoSolicitud estado) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud).orElse(null);
        if (solicitud != null) {
            solicitud.setNumeroConvenio(numeroConvenio);
            solicitud.setEmpresa(empresa);
            solicitud.setCif(cif);
            solicitud.setTutorEmpresa(tutorEmpresa);
            solicitud.setDireccion(direccion);
            solicitud.setLocalidad(localidad);
            solicitud.setCp(cp);
            solicitud.setCicloFormativo(cicloFormativo);
            solicitud.setUsuario(usuario);
            solicitud.setFechaInicio(fechaInicio);
            solicitud.setFechaFin(fechaFin);
            solicitud.setHorasDia(horasDia);
            solicitud.setHorasTotales(horasTotales);
            solicitud.setHorario(horario);
            solicitud.setObservaciones(observaciones);
            solicitud.setEstado(estado);
            solicitudRepository.save(solicitud);
        } else {
            throw new RuntimeException("Solicitud no encontrada con el ID: " + idSolicitud);
        }
    }

    public void update(String idSolicitud, String numeroConvenio, String empresa, String cif, String tutorEmpresa,
            String direccion, String localidad, String cp, Grados cicloFormativo, Usuario usuario,
            LocalDate fechaInicio, LocalDate fechaFin, int horasDia, int horasTotales, String horario,
            String observaciones, EstadoSolicitud estado, Usuario usuarioModificador) throws Exception {
        if (usuarioModificador.getRol() != RolUsuario.PROFESOR) {
            throw new Exception("Solo los profesores pueden modificar solicitudes");
        }

        Solicitud solicitud = solicitudRepository.findById(idSolicitud).orElse(null);
        if (solicitud != null) {
            solicitud.setNumeroConvenio(numeroConvenio);
            solicitud.setEmpresa(empresa);
            solicitud.setCif(cif);
            solicitud.setTutorEmpresa(tutorEmpresa);
            solicitud.setDireccion(direccion);
            solicitud.setLocalidad(localidad);
            solicitud.setCp(cp);
            solicitud.setCicloFormativo(cicloFormativo);
            solicitud.setUsuario(usuario);
            solicitud.setFechaInicio(fechaInicio);
            solicitud.setFechaFin(fechaFin);
            solicitud.setHorasDia(horasDia);
            solicitud.setHorasTotales(horasTotales);
            solicitud.setHorario(horario);
            solicitud.setObservaciones(observaciones);
            solicitud.setEstado(estado);
            solicitudRepository.save(solicitud);
        } else {
            throw new RuntimeException("Solicitud no encontrada con el ID: " + idSolicitud);
        }
    }

    public void cambiarEstadoSolicitud(String idSolicitud, EstadoSolicitud nuevoEstado, Usuario usuarioCambiadorEstado)
            throws Exception {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud).orElse(null);
        solicitud.setEstado(nuevoEstado);
        solicitudRepository.save(solicitud);
    }

    public void delete(String idSolicitud) {
        solicitudRepository.deleteById(idSolicitud);
    }

    public Solicitud save(Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }

    public List<Solicitud> findAllByUsuario(Usuario usuario) {
        return solicitudRepository.findAllByUsuario(usuario);
    }

    public List<Solicitud> findAllByUsuarioAndEstado(Usuario usuario, EstadoSolicitud estadoSolicitud) {
        return solicitudRepository.findAllByUsuarioAndEstado(usuario, estadoSolicitud);
    }

    public List<Solicitud> findAllByUsuarioJefatura(Usuario usuario) {
        return solicitudRepository.findAllByUsuarioJefatura(usuario);
    }

}
