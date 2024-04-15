package org.agaray.spring.pap2024.repository;

import org.agaray.spring.pap2024.domain.Aficion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AficionRepository extends JpaRepository<Aficion,Long> {
    
}
