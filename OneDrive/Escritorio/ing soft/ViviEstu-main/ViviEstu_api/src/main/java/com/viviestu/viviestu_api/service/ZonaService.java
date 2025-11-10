package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.ZonaComentarioDTO;
import com.viviestu.viviestu_api.dto.ZonaReporteDTO;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ZonaService {
    @Autowired
    private ZonaRepository zonaRep;

    public Zona listId(int idZona) {
        return zonaRep.findById(idZona).orElse(null);
    }
    public void edit(Zona z) {
        zonaRep.save(z);
    }
    public void insert(Zona z) { zonaRep.save(z); }
    public List<ZonaComentarioDTO> obtenerPromediosYComentarios() {
        List<Object[]> resultados = zonaRep.obtenerPromediosYComentarios();
        List<ZonaComentarioDTO> lista = new ArrayList<>();
        for (Object[] fila : resultados) {
            ZonaComentarioDTO dto = new ZonaComentarioDTO();
            dto.setIdZona(((Number) fila[0]).intValue());
            dto.setNombreZona((String) fila[1]);
            dto.setPromedioCalificacion(
                    fila[2] != null ? ((Number) fila[2]).doubleValue() : null
            );
            dto.setComentario((String) fila[3]);
            dto.setFecha(fila[4] != null ? ((Timestamp) fila[4]).toLocalDateTime() : null);
            lista.add(dto);
        }
        return lista;
    }
    public List<ZonaReporteDTO> listarZonasQueCumplen(Long idUsuario) {
        List<Object[]> filas = zonaRep.listarZonasQueCumplen(idUsuario);
        List<ZonaReporteDTO> out = new ArrayList<>();

        for (Object[] f : filas) {
            ZonaReporteDTO dto = new ZonaReporteDTO();
            dto.setIdZona(((Number) f[0]).intValue());
            dto.setNombre((String) f[1]);
            dto.setPrecioPromedio(f[2] != null ? ((Number) f[2]).doubleValue() : null);
            dto.setSeguridad((String) f[3]);
            out.add(dto);
        }
        return out;
    }


}
