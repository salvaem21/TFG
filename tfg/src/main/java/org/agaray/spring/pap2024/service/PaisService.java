package org.agaray.spring.pap2024.service;

import java.util.List;
import org.agaray.spring.pap2024.domain.Pais;
import org.agaray.spring.pap2024.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaisService {
    
    @Autowired
    private PaisRepository paisRepository;



    public List<Pais> findAll() {
        return paisRepository.findAll();
    }

    public void save(String nombre) {
        paisRepository.save(new Pais(nombre));
    }

    public Pais findById(Long idPais) {
        return paisRepository.findById(idPais).get();
    }
    public void update(Long idPais, String nombre) {
        Pais pais = paisRepository.findById(idPais).get();
        pais.setNombre(nombre);
        paisRepository.save(pais);
    }
}
