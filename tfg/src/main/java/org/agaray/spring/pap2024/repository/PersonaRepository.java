package org.agaray.spring.pap2024.repository;

import org.agaray.spring.pap2024.domain.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona,Long> {
    
}
