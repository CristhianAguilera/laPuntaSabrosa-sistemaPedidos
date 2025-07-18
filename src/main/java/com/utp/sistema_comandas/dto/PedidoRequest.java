package com.utp.sistema_comandas.dto;

import java.util.List;

public class PedidoRequest {
    private Long pedidoId; // ID del pedido ya creado
    private List<DetalleRequest> detalles;

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public List<DetalleRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleRequest> detalles) {
        this.detalles = detalles;
    }
}
