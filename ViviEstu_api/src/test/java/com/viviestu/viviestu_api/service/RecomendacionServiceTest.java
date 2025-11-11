package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.response.NotificacionResponse;
import com.viviestu.viviestu_api.model.Preferencia;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.PreferenciaRepository;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecomendacionServiceTest {

    @Mock
    private PreferenciaRepository preferenciaRepository;

    @Mock
    private ZonaRepository zonaRepository;

    @InjectMocks
    private RecomendacionService recomendacionService;

    @Test
    void testGenerarRecomendaciones_Falla_SiUsuarioNoTienePreferencias_RN17() {
        // Arrange (Preparar)
        Long usuarioId = 1L;
        // Simulamos que el repositorio no devuelve preferencias
        when(preferenciaRepository.findByUsuarioIdUsuario(usuarioId)).thenReturn(Collections.emptyList());

        // Act & Assert (Actuar y Verificar)
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recomendacionService.generarRecomendacionesParaUsuario(usuarioId, 5) //
        );

        // Verificamos el mensaje de error (RN-17)
        assertEquals("El usuario no tiene preferencias registradas.", exception.getMessage());
        // Verificamos que no se llegó a buscar zonas
        verify(zonaRepository, never()).findAll();
    }

    @Test
    void testGenerarRecomendaciones_Exitoso_FiltraPorPresupuesto_RN18() {
        // Arrange
        Long usuarioId = 1L;
        Preferencia pref = new Preferencia();
        pref.setPresupuesto(1000f); // RN-18
        pref.setTransporte("bus");
        pref.setSeguridad("alta");

        Zona zonaCara = new Zona();
        zonaCara.setIdZona(1);
        zonaCara.setNombre("Zona Cara");
        zonaCara.setPrecioPromedio(2000.0);

        Zona zonaBarata = new Zona();
        zonaBarata.setIdZona(2);
        zonaBarata.setNombre("Zona Barata");
        zonaBarata.setPrecioPromedio(800.0); // Cumple RN-18

        when(preferenciaRepository.findByUsuarioIdUsuario(usuarioId)).thenReturn(List.of(pref));
        // Simulamos que el repo devuelve solo las que cumplen el presupuesto
        when(zonaRepository.findByPrecioPromedioLessThanEqual(1000.0)).thenReturn(List.of(zonaBarata));

        // Act
        List<NotificacionResponse> recomendaciones = recomendacionService.generarRecomendacionesParaUsuario(usuarioId, 5);

        // Assert
        assertEquals(1, recomendaciones.size());
        assertEquals("Zona Barata", recomendaciones.get(0).nombreZona());
    }
}