package com.utp.sistema_comandas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.utp.sistema_comandas.model.Mesa;
import com.utp.sistema_comandas.model.Pedido;
import com.utp.sistema_comandas.repository.PedidoRepository;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;
    private Mesa mesa;

    @BeforeEach
    void setUp() {
        mesa = new Mesa();
        mesa.setId(1L);
        mesa.setNumero(5);

        pedido = new Pedido();
        pedido.setId(10L);
        pedido.setMesa(mesa);
        pedido.setFinalizado(false);
        pedido.setDetalles(new ArrayList<>());

        lenient().when(pedidoRepository.findById(10L)).thenReturn(Optional.of(pedido));
        lenient().when(pedidoRepository.findByMesaIdAndFinalizadoFalse(1L)).thenReturn(pedido);
    }

    @Test
    void pedidosActivosPorMesa_deberiaRetornarPedidoActivo() {
        Pedido resultado = pedidoService.pedidosActivosPorMesa(1L);
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertFalse(resultado.isFinalizado());
        verify(pedidoRepository).findByMesaIdAndFinalizadoFalse(1L);
    }

    @Test
    void obtenerPorId_existente_deberiaRetornarPedido() {
        Optional<Pedido> resultado = pedidoService.obtenerPorId(10L);
        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get().getId());
        verify(pedidoRepository).findById(10L);
    }

    @Test
    void crearPedido_deberiaActualizarPedido() {
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido resultado = pedidoService.crearPedido(pedido);
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        verify(pedidoRepository).save(pedido);
    }

}
