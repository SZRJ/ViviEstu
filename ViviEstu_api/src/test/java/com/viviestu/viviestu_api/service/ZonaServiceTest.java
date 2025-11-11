package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.FiltroRequest;
import com.viviestu.viviestu_api.dto.response.ZonaResponse;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ZonaServiceTest {

    @Mock
    private ZonaRepository zonaRepository;

    @InjectMocks
    private ZonaService zonaService;

    // Zonas de prueba
    private Zona zonaA; // Característica: distancia, precio bajo
    private Zona zonaB; // Característica: precio alto
    private Zona zonaC; // Característica: nombre "Centro"

    // Clase anónima para simular que la entidad Zona tiene el método getDistancia().
    // Esto es NECESARIO porque el código de servicio usa Reflection.
    private class ZonaConDistancia extends Zona {
        private Double distancia;

        // Constructor simplificado sin latitud/longitud
        public ZonaConDistancia(Integer id, String nombre, Double precio, Double distancia) {
            this.setIdZona(id);
            this.setNombre(nombre);
            this.setPrecioPromedio(precio);
            this.distancia = distancia;
            // Quitamos la configuración de latitud/longitud, ya que no existen en Zona
        }

        // Método usado por Reflection en ZonaService.filtrarZonas
        public Double getDistancia() {
            return distancia;
        }
    }

    @BeforeEach
    void setUp() {
        // Inicializamos ZonaA y ZonaC usando la subclase que incluye getDistancia()
        // (ID, Nombre, Precio, Distancia)
        zonaA = new ZonaConDistancia(1, "Zona Norte", 100.0, 5.0); // 5km
        zonaC = new ZonaConDistancia(3, "Centro Histórico", 200.0, 15.0); // 15km

        // Inicializamos ZonaB usando la clase Zona base
        Zona zonaBOriginal = new Zona();
        zonaBOriginal.setIdZona(2);
        zonaBOriginal.setNombre("Zona Sur");
        zonaBOriginal.setPrecioPromedio(300.0);
        zonaBOriginal.setSeguridad("Media");
        zonaBOriginal.setTransporteDisponible("Bus");
        zonaBOriginal.setRecomendado(false);
        // Quitamos la configuración de latitud/longitud de la zona B
        this.zonaB = zonaBOriginal;
    }

    // --- Test de Filtros Básicos (JPA Specification) ---
    // Prueba el filtro por minPrecio y maxPrecio
    @Test
    void testFiltroPorMinYMaxPrecio() {
        // Simular que el repositorio devuelve solo las zonas que cumplen el rango de precio [50, 250]
        when(zonaRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(zonaA, zonaC));

        // Filtro: Precio entre 50 y 250
        FiltroRequest request = new FiltroRequest("Zona", 50.0, 250.0, null, null, null, null, null, null);

        List<ZonaResponse> resultado = zonaService.filtrarZonas(request);

        // Zona B (300.0) es filtrada por la Specification antes de llegar al servicio.
        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(0).idZona());
        assertEquals(3, resultado.get(1).idZona());
    }

    // --- Test de Filtros y Ordenamiento por Distancia (Reflection) ---
    // Prueba el filtro post-consulta por maxDistancia
    @Test
    void testFiltroPorMaxDistancia() {
        // Simular que el repositorio devuelve todas las zonas (A y C tienen distancia simulada, B no)
        when(zonaRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(zonaA, zonaB, zonaC));

        // Filtro: Distancia máxima de 10.0 km
        FiltroRequest request = new FiltroRequest(null, null, null, null, null, 10.0, null, null, null);

        List<ZonaResponse> resultado = zonaService.filtrarZonas(request);

        // Solo Zona A (5km) debería pasar el filtro post-consulta (15km > 10.0, ZonaB no tiene getDistancia simulada)
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).idZona());
        assertEquals("Zona Norte", resultado.get(0).nombre());
    }

    // Prueba el ordenamiento por getDistancia()
    @Test
    void testOrdenamientoPorDistancia() {
        // Simular que el repositorio devuelve las zonas sin ordenar
        when(zonaRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(zonaC, zonaA, zonaB));

        // Filtro: Ordenar por distancia
        FiltroRequest request = new FiltroRequest(null, null, null, null, null, null, null, null, true); // ordenarPorDistancia = true

        List<ZonaResponse> resultado = zonaService.filtrarZonas(request);

        // Esperado: Zona A (5km) -> Zona C (15km) -> Zona B (al final por no tener getDistancia simulado)
        assertEquals(3, resultado.size());
        assertEquals(1, resultado.get(0).idZona()); // 5km
        assertEquals(3, resultado.get(1).idZona()); // 15km
        assertEquals(2, resultado.get(2).idZona()); // 300.0 (por precio promedio)
    }

    // Prueba el ordenamiento por precio promedio (por defecto)
    @Test
    void testOrdenamientoPorPrecioPorDefecto() {
        // Simular que el repositorio devuelve las zonas sin ordenar
        when(zonaRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(zonaC, zonaA, zonaB));

        // Filtro: No ordenar por distancia (debe ordenar por precio)
        FiltroRequest request = new FiltroRequest(null, null, null, null, null, null, null, null, false); // ordenarPorDistancia = false

        List<ZonaResponse> resultado = zonaService.filtrarZonas(request);

        // El resultado esperado es: Zona A (100.0), Zona C (200.0), Zona B (300.0)
        assertEquals(3, resultado.size());
        assertEquals(1, resultado.get(0).idZona()); // 100.0
        assertEquals(3, resultado.get(1).idZona()); // 200.0
        assertEquals(2, resultado.get(2).idZona()); // 300.0
    }
}