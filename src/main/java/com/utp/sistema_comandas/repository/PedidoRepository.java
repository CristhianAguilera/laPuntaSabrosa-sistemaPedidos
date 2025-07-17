package com.utp.sistema_comandas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.utp.sistema_comandas.model.Pedido;
import com.utp.sistema_comandas.model.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{

    Pedido findByMesaIdAndFinalizadoFalse(Long mesaId);

    List<Pedido> findByFinalizado(boolean finalizado);

    List<Pedido> findByMozo(Usuario mozo);
    
}
