package com.salvaceloisma.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salvaceloisma.tfg.domain.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa,Long> {
    public List<Empresa> findByNombre(String nombre);
}
