package com.mol21.Service_DeliveryRice.model.DTO;

import com.mol21.Service_DeliveryRice.model.EstadoPedido;
import com.mol21.Service_DeliveryRice.model.MetodoPago;
import com.mol21.Service_DeliveryRice.model.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoDTORepartidor {
    private long id;
    private EstadoPedido estadoPedido;
    private BigDecimal precioTotal;
    private int totalProductos;
    private MetodoPago metodoPago;
    private LocalDateTime fechaCreacion;
    private DireccionDTO direccion;
    private UsuarioDTO cliente;
    private List<DetalleDTO> listaDetalles;
    private long idRepartidor;

    public PedidoDTORepartidor(Pedido pedido, List<DetalleDTO> pedidosDTOS) {
        this.id = pedido.getId_pedido();
        this.direccion = new DireccionDTO(pedido.getDireccionEnvio());
        this.precioTotal = pedido.getTotalPrecio();
        this.totalProductos = pedido.getDetalles().size();
        this.estadoPedido = pedido.getEstadoPedido();
        this.metodoPago = pedido.getMetodoPago();
        this.listaDetalles = pedidosDTOS;
        this.cliente = new UsuarioDTO(pedido.getUsuario());
        this.fechaCreacion = pedido.getFechaCreacion();
        this.idRepartidor = pedido.getRepartidor().get_id();
    }
public PedidoDTORepartidor(Pedido pedido){
    this.id = pedido.getId_pedido();
    this.direccion = new DireccionDTO(pedido.getDireccionEnvio());
    this.precioTotal = pedido.getTotalPrecio();
    this.totalProductos = pedido.getDetalles().size();
    this.estadoPedido = pedido.getEstadoPedido();
    this.metodoPago = pedido.getMetodoPago();
    this.cliente = new UsuarioDTO(pedido.getUsuario());
    this.fechaCreacion = pedido.getFechaCreacion();
    this.idRepartidor = pedido.getRepartidor().get_id();
}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public int getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(int totalProductos) {
        this.totalProductos = totalProductos;
    }

    public DireccionDTO getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionDTO direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public UsuarioDTO getCliente() {
        return cliente;
    }

    public void setCliente(UsuarioDTO cliente) {
        this.cliente = cliente;
    }

    public List<DetalleDTO> getListaDetalles() {
        return listaDetalles;
    }

    public void setListaDetalles(List<DetalleDTO> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }
}
