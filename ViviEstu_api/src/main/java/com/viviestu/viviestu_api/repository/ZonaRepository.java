package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ZonaRepository extends JpaRepository<Zona, Integer> {

    // Buscar por transporte disponible (contiene)
    List<Zona> findByTransporteDisponibleContainingIgnoreCase(String transporte);

    // Buscar por seguridad exacta o que contenga
    List<Zona> findBySeguridadContainingIgnoreCase(String seguridad);

    // Buscar por precio menor o igual
    List<Zona> findByPrecioPromedioLessThanEqual(Double maxPrecio);

    @Query(value = "SELECT \n" +
            "    z.id_zona,\n" +
            "    z.nombre AS nombre_zona,\n" +
            "    AVG(ca.puntuacion) AS promedio_calificacion,\n" +
            "    co.comentario,\n" +
            "    co.fecha\n" +
            " FROM zonas z\n" +
            " LEFT JOIN calificaciones ca ON ca.id_zona = z.id_zona\n" +
            " LEFT JOIN comentarios co ON co.id_zona = z.id_zona\n" +
            " GROUP BY z.id_zona, z.nombre, co.comentario, co.fecha\n" +
            " ORDER BY z.id_zona ASC, co.fecha ASC ", nativeQuery = true)
    List<Object[]> obtenerPromediosYComentarios();

}
