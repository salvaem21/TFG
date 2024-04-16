package com.salvaceloisma.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salvaceloisma.tfg.domain.Empresa;
import com.salvaceloisma.tfg.repository.EmpresaRepository;

@Service
public class EmpresaService {
    
    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    public List<Empresa> findByNombre(String nombre) {
        return empresaRepository.findByNombre(nombre);
    }

    public Empresa save(String nombre) {
        return empresaRepository.save(new Empresa(null, nombre, nombre, nombre, 0));
    }

    public Empresa findById(Long idEmpresa) {
        return empresaRepository.findById(idEmpresa).get();
    }
    public void update(Long idEmpresa, String nombre) {
        Empresa empresa = empresaRepository.findById(idEmpresa).get();
        empresa.setNombre(nombre);
        empresaRepository.save(empresa);
    }

    public void delete(Long idEmpresa) {
        empresaRepository.delete(empresaRepository.getReferenceById(idEmpresa));
    }
}
