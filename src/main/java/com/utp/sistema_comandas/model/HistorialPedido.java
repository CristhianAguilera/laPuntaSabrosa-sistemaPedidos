package com.utp.sistema_comandas.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "historial_pedidos")
public class HistorialPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    private double total;

    @ManyToOne
    private Usuario mozo;

    @ManyToOne
    private Mesa mesa;

    @ManyToOne
    private Pedido pedido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Usuario getMozo() {
        return mozo;
    }

    public void setMozo(Usuario mozo) {
        this.mozo = mozo;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
 
}
