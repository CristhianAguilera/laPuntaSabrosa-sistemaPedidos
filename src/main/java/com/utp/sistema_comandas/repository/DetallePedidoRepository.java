package com.utp.sistema_comandas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utp.sistema_comandas.model.DetallePedido;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long>{
    
    List<DetallePedido> findByPedidoId(Long pedidoId);
}
