package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.model.Favoritos;
import com.viviestu.viviestu_api.service.FavoritosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoritosController.class)
public class FavoritosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoritosService service;

    @Test
    void listAll_returnsJsonArray() throws Exception {
        Favoritos f = new Favoritos("t","d"); f.setId(1L);
        when(service.getAll()).thenReturn(List.of(f));

        mockMvc.perform(get("/api/favoritos"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1));

        verify(service).getAll();
    }

    @Test
    void create_returnsCreatedAndLocation() throws Exception {
        Favoritos saved = new Favoritos("t","d"); saved.setId(5L);
        when(service.create(any(Favoritos.class))).thenReturn(saved);
        String json = "{\"title\":\"t\",\"description\":\"d\"}";

        mockMvc.perform(post("/api/favoritos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location","/api/favoritos/5"))
            .andExpect(jsonPath("$.id").value(5));

        verify(service).create(any(Favoritos.class));
    }

    @Test
    void delete_whenPresent_returnsNoContent() throws Exception {
        Favoritos f = new Favoritos("t","d"); f.setId(3L);
        when(service.getById(3L)).thenReturn(Optional.of(f));
        doNothing().when(service).delete(3L);

        mockMvc.perform(delete("/api/favoritos/3"))
            .andExpect(status().isNoContent());

        verify(service).getById(3L);
        verify(service).delete(3L);
    }

    @Test
    void delete_whenNotFound_returns404() throws Exception {
        when(service.getById(10L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/favoritos/10"))
            .andExpect(status().isNotFound());

        verify(service).getById(10L);
        verify(service, never()).delete(anyLong());
    }
}
