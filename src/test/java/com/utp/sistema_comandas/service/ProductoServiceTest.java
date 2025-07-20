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

import com.utp.sistema_comandas.model.Producto;
import com.utp.sistema_comandas.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void guardarProducto_deberiaGuardarCorrectamente() {
        // Arrange
        Producto producto = new Producto();
        producto.setNombre("Pollo a la brasa");
        producto.setPrecio(25.0);
        producto.setTipo("Carta");

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // Act
        Producto resultado = productoService.guardarProducto(producto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Pollo a la brasa", resultado.getNombre());
        assertEquals(25.0, resultado.getPrecio());
        assertEquals("Carta", resultado.getTipo());
        verify(productoRepository).save(producto);
    }

    @Test
    void obtenerProductoPorId_existente_deberiaRetornarProducto() {
        // Arrange
        Long id = 1L;
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Lomo saltado");

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        // Act
        Optional<Producto> resultado = productoService.obtenerPorId(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Lomo saltado", resultado.get().getNombre());
        verify(productoRepository).findById(id);
    }

    @Test
    void obtenerProductoPorId_noExistente_deberiaRetornarVacio() {
        Long id = 2L;

        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoService.obtenerPorId(id);

        assertFalse(resultado.isPresent());
        verify(productoRepository).findById(id);
    }

    @Test
    void eliminarProducto_existente_deberiaEliminarCorrectamente() {
        Long id = 1L;

        doNothing().when(productoRepository).deleteById(id);

        productoService.eliminar(id);

        verify(productoRepository, times(1)).deleteById(id);
    }
    
}
