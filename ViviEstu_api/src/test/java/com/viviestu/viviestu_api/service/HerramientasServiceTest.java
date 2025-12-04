package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.PreferenciaRequest;
import com.viviestu.viviestu_api.dto.request.SimuladorRequest;
import com.viviestu.viviestu_api.dto.response.*;
import com.viviestu.viviestu_api.model.*;
import com.viviestu.viviestu_api.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class HerramientasServiceTest {

    @Mock private PreferenciaRepository preferenciaRepository;
    @Mock private ZonaRepository zonaRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private FavoritoRepository favoritoRepository;
    @Mock private CalificacionRepository calificacionRepository;
    @Mock private ComentarioRepository comentarioRepository;

    @InjectMocks private PreferenciaService preferenciaService;
    @InjectMocks private TransporteService transporteService;
    @InjectMocks private SimuladorService simuladorService;
    @InjectMocks private ResumenService resumenService;
    @InjectMocks private RecomendacionService recomendacionService;

    // --- US03: PREFERENCIAS ---
    @Test
    void testCrearPreferencia_US03() {
        PreferenciaRequest req = new PreferenciaRequest(1L, "UPC", 1000f, "Bus", 30f, "Alta");
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(new Usuario()));
        when(preferenciaRepository.save(any(Preferencia.class))).thenAnswer(i -> i.getArgument(0));

        PreferenciaResponse res = preferenciaService.crear(req);
        assertEquals("UPC", res.universidad());
    }

    // --- US07: TRANSPORTE ---
    @Test
    void testCalcularTiempo_US07() {
        TiempoTransporteResponse res = transporteService.calcularTiempo(1, "Universidad", "Bus");
        assertNotNull(res.tiempoEstimado());
    }

    // --- US12: NOTIFICACIONES (CORREGIDO) ---
    @Test
    void testGenerarRecomendaciones_US12() {
        // 1. Preferencia completa (minúsculas para asegurar coincidencia)
        Preferencia pref = new Preferencia();
        pref.setPresupuesto(1000f);
        pref.setSeguridad("alta");
        pref.setTransporte("bus");

        // 2. Zona completa que coincide
        Zona z = new Zona();
        z.setIdZona(1);
        z.setNombre("Zona Perfecta");
        z.setPrecioPromedio(800.0);
        z.setSeguridad("alta");
        z.setTransporteDisponible("bus");

        // Mocks
        when(preferenciaRepository.findByUsuarioIdUsuario(anyLong())).thenReturn(List.of(pref));
        // Mockeamos ambas búsquedas por seguridad
        when(zonaRepository.findByPrecioPromedioLessThanEqual(anyDouble())).thenReturn(List.of(z));
        when(zonaRepository.findAll()).thenReturn(List.of(z));

        List<NotificacionResponse> res = recomendacionService.generarRecomendacionesParaUsuario(1L, 5);

        assertNotNull(res);
        assertEquals(1, res.size(), "El filtro debería encontrar 1 zona coincidente");
        assertEquals("Zona Perfecta", res.get(0).nombreZona());
    }

    // --- US17: SIMULADOR DE GASTO ---
    @Test
    void testSimularGasto_US17() {
        SimuladorRequest req = new SimuladorRequest(1L, 1000.0, 200.0);
        SimuladorResponse res = simuladorService.simularGasto(req);
        assertEquals(1200.0, res.gastoTotal());
    }

    // --- US20: RESUMEN DE BÚSQUEDA ---
    @Test
    void testObtenerResumen_US20() {
        when(favoritoRepository.findByUsuarioIdUsuario(anyLong())).thenReturn(Collections.emptyList());
        when(calificacionRepository.countByUsuarioIdUsuario(anyLong())).thenReturn(5L);
        when(comentarioRepository.countByUsuarioIdUsuario(anyLong())).thenReturn(3L);

        ResumenResponse res = resumenService.obtenerResumen(1L);
        assertEquals(5, res.totalCalificaciones());
        assertEquals(3, res.totalComentarios());
    }
}