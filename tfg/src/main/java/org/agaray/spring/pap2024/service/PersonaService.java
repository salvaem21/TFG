package org.agaray.spring.pap2024.service;

import java.util.ArrayList;
import java.util.List;
import org.agaray.spring.pap2024.domain.Aficion;
import org.agaray.spring.pap2024.domain.Persona;
import org.agaray.spring.pap2024.repository.AficionRepository;
import org.agaray.spring.pap2024.repository.PaisRepository;
import org.agaray.spring.pap2024.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private AficionRepository aficionRepository;

    public List<Persona> findAll() {
        return personaRepository.findAll();
    }

    public void save(
            String dni,
            String nombre,
            Long idNace,
            Long idVive,
            List<Long> idsGusto,
            List<Long> idsOdio) {
        Persona persona = new Persona(dni, nombre);
        persona.setNace(paisRepository.findById(idNace).get());
        persona.setVive(paisRepository.findById(idVive).get());

        idsGusto = (idsGusto == null ? new ArrayList<Long>() : idsGusto);
        for (Long idGusto : idsGusto) {
            Aficion gusto = aficionRepository.findById(idGusto).get();
            persona.getGustos().add(gusto);
        }
        idsOdio = (idsOdio == null ? new ArrayList<Long>() : idsOdio);
        for (Long idOdio : idsOdio) {
            Aficion odio = aficionRepository.findById(idOdio).get();
            persona.getOdios().add(odio);
        }

        personaRepository.save(persona);

    }

    public Persona findById(Long idPersona) {
        return personaRepository.findById(idPersona).get();
    }

    public void update(Long idPersona, String dni, String nombre, Long idNace, Long idVive, List<Long> idsGusto,
            List<Long> idsOdio) {

        Persona persona = personaRepository.findById(idPersona).get();

        persona.setDni(dni);
        persona.setNombre(nombre);

        persona.setNace(paisRepository.findById(idNace).get());
        persona.setVive(paisRepository.findById(idVive).get());

        idsGusto = (idsGusto == null ? new ArrayList<Long>() : idsGusto);
        List<Aficion> nuevosGustos = new ArrayList<Aficion>();
        for (Long idGusto : idsGusto) {
            Aficion gusto = aficionRepository.findById(idGusto).get();
            nuevosGustos.add(gusto);
        }
        persona.setGustos(nuevosGustos);
        
        idsOdio = (idsOdio == null ? new ArrayList<Long>() : idsOdio);
        List<Aficion> nuevosOdios = new ArrayList<Aficion>();
        for (Long idOdio : idsOdio) {
            Aficion odio = aficionRepository.findById(idOdio).get();
            nuevosOdios.add(odio);
        }
        persona.setOdios(nuevosOdios);
        

        personaRepository.save(persona);
    }
}
