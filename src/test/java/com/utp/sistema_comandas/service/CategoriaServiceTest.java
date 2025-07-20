package com.utp.sistema_comandas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.utp.sistema_comandas.model.Categoria;
import com.utp.sistema_comandas.repository.CategoriaRepository;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void guardarCategoria_deberiaGuardarCorrectamente() {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setNombre("Entradas");

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        // Act
        Categoria resultado = categoriaService.guardarCategoria(categoria);

        // Assert
        assertNotNull(resultado);
        assertEquals("Entradas", resultado.getNombre());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void obtenerCategoriaPorId_existente_deberiaRetornarCategoria() {
        // Arrange
        Long id = 1L;
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNombre("Bebidas");

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));

        // Act
        Optional<Categoria> resultado = categoriaService.obtenerPorId(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Bebidas", resultado.get().getNombre());
        verify(categoriaRepository).findById(id);
    }

    @Test
    void obtenerCategoriaPorId_noExistente_deberiaRetornarVacio() {
        Long id = 2L;

        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Categoria> resultado = categoriaService.obtenerPorId(id);

        assertFalse(resultado.isPresent());
        verify(categoriaRepository).findById(id);
    }

    @Test
    void eliminarCategoria_existente_deberiaEliminarCorrectamente() {
        Long id = 1L;

        doNothing().when(categoriaRepository).deleteById(id);

        categoriaService.eliminarCategoria(id);

        verify(categoriaRepository, times(1)).deleteById(id);
    }

    
}
