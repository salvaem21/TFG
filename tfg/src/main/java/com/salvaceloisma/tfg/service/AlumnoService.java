package com.salvaceloisma.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salvaceloisma.tfg.domain.Alumno;
import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.repository.AlumnoRepository;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private SolicitudService solicitudService;

    public List<Alumno> findAll() {
        return alumnoRepository.findAll();
    }

    public List<Alumno> findByDni(String dni) {
        return alumnoRepository.findByDni(dni);
    }

    public Alumno save(String dni, String nombre, String apellido, String idSolicitud) {
        Alumno alumno = new Alumno(dni, nombre, apellido);
        Solicitud solicitud = solicitudService.findById(idSolicitud);
        alumno.setSolicitud(solicitud);
        return alumnoRepository.save(alumno);
    }

    public Alumno updateByDni(String dni, String nombre, String apellido, String idSolicitud) {
        List<Alumno> alumnos = alumnoRepository.findByDni(dni);
        if (!alumnos.isEmpty()) {
            Alumno alumno = alumnos.get(0);
            alumno.setNombre(nombre);
            alumno.setApellido(apellido);
            if (idSolicitud != null && !idSolicitud.isEmpty()) {
                Solicitud solicitud = solicitudService.findById(idSolicitud);
                alumno.setSolicitud(solicitud);
            }
            return alumnoRepository.save(alumno);
        } else {
            throw new RuntimeException("No se encontró ningún alumno con DNI: " + dni);
        }
    }

    public Alumno findById(Long idAlumno) {
        return alumnoRepository.findById(idAlumno).get();
    }

    public List<Alumno> findBySolicitudIdSolicitud(String idSolicitud) {
        return alumnoRepository.findBySolicitudIdSolicitud(idSolicitud);
    }

    public void update(Long idAlumno, String dni, String nombre, String apellido) {
        Alumno alumno = alumnoRepository.findById(idAlumno).get();
        alumno.setDni(dni);
        alumno.setNombre(nombre);
        alumno.setApellido(apellido);
        alumnoRepository.save(alumno);
    }

    public void delete(Long idAlumno) {
        alumnoRepository.delete(alumnoRepository.getReferenceById(idAlumno));
    }

    public void deleteByDniAndSolicitud(String dni, String idSolicitud) {
        alumnoRepository.deleteByDniAndSolicitudIdSolicitud(dni, idSolicitud);
    }

    public Alumno findByDniAndSolicitudIdSolicitud(String dni, String idSolicitud) {
        return alumnoRepository.findByDniAndSolicitudIdSolicitud(dni, idSolicitud);
    }

    @Transactional
    public void deleteAllBySolicitud(Solicitud solicitud) {
        alumnoRepository.deleteAllBySolicitud(solicitud);

    }

}
