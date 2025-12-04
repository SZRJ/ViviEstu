package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.FiltroRequest;
import com.viviestu.viviestu_api.dto.response.ZonaReporteResponse;
import com.viviestu.viviestu_api.dto.response.ZonaResponse;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList; // Importante para listas modificables
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ZonaServiceTest {

    @Mock private ZonaRepository zonaRepository;
    @InjectMocks private ZonaService zonaService;

    // --- US04: ZONAS SUGERIDAS ---
    @Test
    void testListarZonasQueCumplen_US04() {
        Object[] row = {1, "Zona Ideal", 1000.0, "ALTA"};
        when(zonaRepository.listarZonasQueCumplen(1L)).thenReturn(Collections.singletonList(row));

        List<ZonaReporteResponse> res = zonaService.listarZonasQueCumplen(1L);

        assertFalse(res.isEmpty());
        assertEquals("Zona Ideal", res.get(0).nombre());
    }

    // --- US05: CONSULTAR FICHAS ---
    @Test
    void testObtenerPorId_US05() {
        Integer idZona = 1;
        Zona z = new Zona();
        z.setIdZona(idZona);
        z.setNombre("Miraflores");
        z.setPrecioPromedio(2500.0);

        when(zonaRepository.findById(idZona)).thenReturn(Optional.of(z));

        ZonaResponse response = zonaService.obtenerZonaPorId(idZona);

        assertNotNull(response);
        assertEquals("Miraflores", response.nombre());
    }

    // --- US08: FILTROS DE BÚSQUEDA ---
    @Test
    void testFiltrarZonas_US08() {
        FiltroRequest filtro = new FiltroRequest("Lince", null, null, null, null, null, null, null, null);

        Zona zonaEncontrada = new Zona();
        zonaEncontrada.setNombre("Lince");
        zonaEncontrada.setPrecioPromedio(1500.0); // Precio necesario para el sort

        // CORRECCIÓN: Usamos new ArrayList(...) para que la lista sea modificable (mutable)
        // Esto evita el UnsupportedOperationException cuando el servicio intenta ordenar la lista
        List<Zona> listaModificable = new ArrayList<>();
        listaModificable.add(zonaEncontrada);

        when(zonaRepository.findAll(any(Specification.class))).thenReturn(listaModificable);

        List<ZonaResponse> resultados = zonaService.filtrarZonas(filtro);

        assertFalse(resultados.isEmpty());
        assertEquals("Lince", resultados.get(0).nombre());
    }

    // --- US15: MARCAR COMO RECOMENDADA ---
    @Test
    void testMarcarRecomendacion_US15() {
        Integer idZona = 1;
        Zona z = new Zona();
        z.setIdZona(idZona);
        z.setRecomendado(false);

        when(zonaRepository.findById(idZona)).thenReturn(Optional.of(z));
        when(zonaRepository.save(any(Zona.class))).thenAnswer(i -> i.getArgument(0));

        ZonaResponse res = zonaService.marcarRecomendacion(idZona, true);

        assertTrue(res.recomendado());
    }
}