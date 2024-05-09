package com.salvaceloisma.tfg.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Alumno save(String dni,String nombre, String apellido,LocalDate fechaNacimiento, String idSolicitud) {
        Alumno alumno = new Alumno(dni, nombre, apellido, fechaNacimiento);
        Solicitud solicitud = solicitudService.findById(idSolicitud);
        alumno.setSolicitud(solicitud);
        return alumnoRepository.save(alumno);
    }

    public Alumno findById(Long idAlumno) {
        return alumnoRepository.findById(idAlumno).get();
    }

    public List<Alumno> findBySolicitudIdSolicitud(String idSolicitud) {
        return alumnoRepository.findBySolicitudIdSolicitud(idSolicitud);
    }
    
    public void update(Long idAlumno,String dni,String nombre, String apellido,LocalDate fechaNacimiento) {
        Alumno alumno = alumnoRepository.findById(idAlumno).get();
        alumno.setDni(dni);
        alumno.setNombre(nombre);
        alumno.setApellido(apellido);
        alumno.setFechaNacimiento(fechaNacimiento);
        alumnoRepository.save(alumno);
    }

    public void delete(Long idAlumno) {
        alumnoRepository.delete(alumnoRepository.getReferenceById(idAlumno));
    }
}
