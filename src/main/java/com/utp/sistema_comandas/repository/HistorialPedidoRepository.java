package com.utp.sistema_comandas.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.utp.sistema_comandas.model.HistorialPedido;

@Repository
public interface HistorialPedidoRepository extends JpaRepository<HistorialPedido, Long> {
    
    List<HistorialPedido> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    List<HistorialPedido> findByMozoId(Long mozoId);
    
}
