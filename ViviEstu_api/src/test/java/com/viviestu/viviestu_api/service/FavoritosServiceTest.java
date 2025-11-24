package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.model.Favoritos;
import com.viviestu.viviestu_api.repository.FavoritosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class FavoritosServiceTest {

    @Mock
    private FavoritosRepository repository;

    @InjectMocks
    private FavoritosService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_returnsList() {
        List<Favoritos> list = List.of(new Favoritos("a","b"));
        when(repository.findAll()).thenReturn(list);

        List<Favoritos> res = service.getAll();

        assertThat(res).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void create_savesAndReturns() {
        Favoritos f = new Favoritos("t","d");
        Favoritos saved = new Favoritos("t","d");
        saved.setId(1L);
        when(repository.save(f)).thenReturn(saved);

        Favoritos out = service.create(f);

        assertThat(out.getId()).isEqualTo(1L);
        verify(repository).save(f);
    }

    @Test
    void delete_callsRepository() {
        doNothing().when(repository).deleteById(1L);
        service.delete(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void getById_returnsOptional() {
        Favoritos f = new Favoritos("t","d");
        f.setId(2L);
        when(repository.findById(2L)).thenReturn(Optional.of(f));

        Optional<Favoritos> res = service.getById(2L);

        assertThat(res).isPresent();
        assertThat(res.get().getId()).isEqualTo(2L);
        verify(repository).findById(2L);
    }
}
 