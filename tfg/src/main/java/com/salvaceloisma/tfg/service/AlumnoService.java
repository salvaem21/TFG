package com.salvaceloisma.tfg.service;

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

    public List<Alumno> findAll() {
        return alumnoRepository.findAll();
    }

    public List<Alumno> findByDni(String dni) {
        return alumnoRepository.findByDni(dni);
    }

    public Alumno save(String dni,String nombre, String apellido,Solicitud solicitud) {
        return alumnoRepository.save(new Alumno(dni, nombre, apellido, solicitud));
    }

    public Alumno findById(Long idAlumno) {
        return alumnoRepository.findById(idAlumno).get();
    }
    public void update(Long idAlumno,String dni,String nombre, String apellido,Solicitud solicitud) {
        Alumno alumno = alumnoRepository.findById(idAlumno).get();
        alumno.setDni(dni);
        alumno.setNombre(nombre);
        alumno.setApellido(apellido);
        alumno.setSolicitud(solicitud);
        alumnoRepository.save(alumno);
    }

    public void delete(Long idAlumno) {
        alumnoRepository.delete(alumnoRepository.getReferenceById(idAlumno));
    }
}
