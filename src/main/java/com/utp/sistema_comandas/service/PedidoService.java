package com.utp.sistema_comandas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utp.sistema_comandas.model.Pedido;
import com.utp.sistema_comandas.model.Usuario;
import com.utp.sistema_comandas.repository.PedidoRepository;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Pedido pedidosActivosPorMesa(Long mesaId) {
        return pedidoRepository.findByMesaIdAndFinalizadoFalse(mesaId);
    }

    public Optional<Pedido> obtenerPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> obtenerPedidosPorMozo(Usuario mozo) {
        return pedidoRepository.findByMozo(mozo);
    }

    @Transactional
    public Pedido finalizarPedido(Pedido pedido) {
        pedido.setFinalizado(true);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarFinalizados() {
        return pedidoRepository.findByFinalizado(true);
    }

}
