package com.utp.sistema_comandas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utp.sistema_comandas.model.DetallePedido;
import com.utp.sistema_comandas.repository.DetallePedidoRepository;

@Service
public class DetallePedidoService {
    
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    public List<DetallePedido> listarPorPedido(Long pedidoId) {
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }

    public DetallePedido guardar(DetallePedido detalle) {
        return detallePedidoRepository.save(detalle);
    }

    public void eliminar(Long id) {
        detallePedidoRepository.deleteById(id);
    }
}
