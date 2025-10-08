package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ZonasRepository extends JpaRepository<Zona, Integer> {
    // Buscar por transporte disponible (contiene)
    List<Zona> findByTransporteDisponibleContainingIgnoreCase(String transporte);

    // Buscar por seguridad exacta o que contenga
    List<Zona> findBySeguridadContainingIgnoreCase(String seguridad);

    // Buscar por precio menor o igual
    List<Zona> findByPrecioPromedioLessThanEqual(Double maxPrecio);

    // Composición simple para recomendaciones: we'll call multiple filters in service
}
