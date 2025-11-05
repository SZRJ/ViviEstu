// Archivo: repository/ZonaRepository.java
package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // <-- AÑADIR ESTA IMPORTACIÓN
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/// Repositorio de acceso a datos para zonas
public interface ZonaRepository extends JpaRepository<Zona, Integer>, JpaSpecificationExecutor<Zona> { // <-- AÑADIR JpaSpecificationExecutor

    List<Zona> findBySeguridadContainingIgnoreCase(String seguridad);
    List<Zona> findByTransporteDisponibleContainingIgnoreCase(String transporte);
    List<Zona> findByPrecioPromedioLessThanEqual(Double precioMax);

    /*
     * ESTA ES LA CONSULTA CORREGIDA (Usa ROW_NUMBER() para obtener el último comentario)
     * Reemplaza la consulta nativa "obtenerPromediosYComentarios" con esta.
     */
    @Query(value = """
        WITH UltimoComentario AS (
            SELECT 
                c.id_zona, c.comentario, c.fecha,
                ROW_NUMBER() OVER(PARTITION BY c.id_zona ORDER BY c.fecha DESC) as rn
            FROM comentarios c
        )
        SELECT 
            z.id_zona,
            z.nombre AS nombre_zona,
            AVG(ca.puntuacion) AS promedio_calificacion,
            uc.comentario,
            uc.fecha
        FROM zonas z
        LEFT JOIN calificaciones ca ON ca.id_zona = z.id_zona
        LEFT JOIN UltimoComentario uc ON uc.id_zona = z.id_zona AND uc.rn = 1
        GROUP BY z.id_zona, z.nombre, uc.comentario, uc.fecha
        ORDER BY z.id_zona ASC
    """, nativeQuery = true)
    List<Object[]> obtenerPromediosYComentarios();
    /// Lista zonas recomendadas según preferencias del usuario
    @Query(value = """
        SELECT DISTINCT
            z.id_zona,
            z.nombre,
            z.precio_promedio,
            z.seguridad
        FROM zonas z
        WHERE
            LOWER(z.seguridad) IN ('media','alta')
            AND EXISTS (
                SELECT 1
                FROM preferencias p
                WHERE p.id_usuario = :idUsuario
                  AND p.presupuesto IS NOT NULL
                  AND z.precio_promedio <= p.presupuesto
            )
        ORDER BY z.nombre ASC
    """, nativeQuery = true)
    List<Object[]> listarZonasQueCumplen(@Param("idUsuario") Long idUsuario);

    // --- NUEVO CÓDIGO AÑADIDO (PARA US09) ---
    List<Zona> findByIdZonaIn(List<Integer> ids);
}