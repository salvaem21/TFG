package org.agaray.spring.pap2024.service;

import java.util.List;
import org.agaray.spring.pap2024.domain.Aficion;
import org.agaray.spring.pap2024.repository.AficionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AficionService {

    @Autowired
    private AficionRepository aficionRepository;


    public Aficion findById(Long idAficion) {
        return aficionRepository.findById(idAficion).get();
    }

    public List<Aficion> findAll() {
        return aficionRepository.findAll();
    }

    public void save(String nombre) {
        aficionRepository.save(new Aficion(nombre));
    }

    public void update(Long idAficion, String nombre) {
        Aficion aficion = aficionRepository.findById(idAficion).get();
        aficion.setNombre(nombre);
        aficionRepository.save(aficion);
    }

}
