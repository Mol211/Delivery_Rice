package com.mol21.Service_DeliveryRice.model.DTO;

import com.mol21.Service_DeliveryRice.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoDTOAdmin {
    private long id;//
    private BigDecimal precioTotal;//
    private int totalProductos;
    private EstadoPedido estadoPedido;
    private MetodoPago metodoPago;
    private List<DetalleDTO> listaDetalles;
    private LocalDateTime fechaCreacion;
    private DireccionDTO direccion;
    private UsuarioDTO usuario;
    private UsuarioDTO repartidor;


    public PedidoDTOAdmin(Pedido pedido, List<DetalleDTO> pedidosDTOS) {
        this.id = pedido.getId_pedido();
        this.direccion = new DireccionDTO(pedido.getDireccionEnvio());
        this.precioTotal = pedido.getTotalPrecio();
        this.totalProductos = pedido.getDetalles().size();
        this.estadoPedido = pedido.getEstadoPedido();
        this.metodoPago = pedido.getMetodoPago();
        this.listaDetalles = pedidosDTOS;
        this.usuario = new UsuarioDTO(pedido.getUsuario());
        this.repartidor = new UsuarioDTO(pedido.getRepartidor());
        this.fechaCreacion = pedido.getFechaCreacion();
    }

    public PedidoDTOAdmin(Pedido pedido) {
        this.id = pedido.getId_pedido();
        this.direccion = new DireccionDTO(pedido.getDireccionEnvio());
        this.precioTotal = pedido.getTotalPrecio();
        this.totalProductos = pedido.getDetalles().size();
        this.estadoPedido = pedido.getEstadoPedido();
        this.metodoPago = pedido.getMetodoPago();
        this.usuario = new UsuarioDTO(pedido.getUsuario());
        this.fechaCreacion = pedido.getFechaCreacion();
        this.repartidor = new UsuarioDTO(pedido.getRepartidor());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(int totalProductos) {
        this.totalProductos = totalProductos;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<DetalleDTO> getListaDetalles() {
        return listaDetalles;
    }

    public void setListaDetalles(List<DetalleDTO> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }

    public UsuarioDTO getRepartidor() {
        return repartidor;
    }
    public void setRepartidor(UsuarioDTO repartidor) {
        this.repartidor = repartidor;
    }
}
