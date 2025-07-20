package com.utp.sistema_comandas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.utp.sistema_comandas.model.Mesa;
import com.utp.sistema_comandas.repository.MesaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class MesaServiceTest {

    @Mock
    private MesaRepository mesaRepository;

    @InjectMocks
    private MesaService mesaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void agregarMesa_deberiaGuardarConNumeroIncrementado() {
        // Simula que la última mesa fue la número 5
        Mesa nuevaMesa = new Mesa();
        nuevaMesa.setNumero(6);
        nuevaMesa.setCantidadPersonas(0);
        nuevaMesa.setMontoTotal(0.0);
        nuevaMesa.setNombreCliente("");
        nuevaMesa.setNombreMozo("");
        nuevaMesa.setOcupada(false);

        // Cuando se guarda, simula que devuelve la misma mesa
        when(mesaRepository.save(any(Mesa.class))).thenReturn(nuevaMesa);

        // Act
        Mesa resultado = mesaService.guardar(nuevaMesa);

        // Assert
        assertEquals(6, resultado.getNumero());
        assertFalse(resultado.isOcupada());
        verify(mesaRepository).save(any(Mesa.class));
    }

    @Test
    void eliminarMesa_existente_deberiaEliminarCorrectamente() {
        Long idMesa = 1L;

        doNothing().when(mesaRepository).deleteById(idMesa);

        // Ejecutamos el método a testear
        mesaService.eliminar(idMesa);

        // Verificamos que se haya llamado deleteById con el ID correcto
        verify(mesaRepository, times(1)).deleteById(idMesa);
    }

}
