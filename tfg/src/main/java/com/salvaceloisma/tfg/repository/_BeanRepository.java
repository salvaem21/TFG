package com.salvaceloisma.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salvaceloisma.tfg.domain._Bean;

@Repository
public interface _BeanRepository extends JpaRepository<_Bean,Long> {
    public List<_Bean> findByNombre(String nombre);
}
