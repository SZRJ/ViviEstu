package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Preferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenciasRepository extends JpaRepository<Preferencia, Long> {
}
