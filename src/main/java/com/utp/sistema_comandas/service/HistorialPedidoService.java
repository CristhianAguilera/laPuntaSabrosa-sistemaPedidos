package com.utp.sistema_comandas.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.utp.sistema_comandas.model.HistorialPedido;
import com.utp.sistema_comandas.repository.HistorialPedidoRepository;

@Service
public class HistorialPedidoService {

    @Autowired
    private HistorialPedidoRepository historialPedidoRepository;

    public HistorialPedido guardar(HistorialPedido historial) {
        historial.setFecha(LocalDateTime.now());
        return historialPedidoRepository.save(historial);
    }

    public List<HistorialPedido> listarPorRango(LocalDateTime inicio, LocalDateTime fin) {
        return historialPedidoRepository.findByFechaBetween(inicio, fin);
    }

    public List<HistorialPedido> listarPorMozo(Long mozoId) {
        return historialPedidoRepository.findByMozoId(mozoId);
    }

    public List<HistorialPedido> listarTodos() {
        return historialPedidoRepository.findAll();
    }
    
}
