package com.mol21.Service_DeliveryRice.model.DTO;

import com.mol21.Service_DeliveryRice.model.*;

import java.math.BigDecimal;
import java.util.List;

public class PedidoDTO {
    private long id;
    private long usuarioId;
    private long direccionId;
    private String nombreUsuario;
    private String email;
    private BigDecimal precioTotal;
    private int totalProductos;
    private EstadoPedido estadoPedido;
    private MetodoPago metodoPago;
    private List<DetalleDTO> listaDetalles;

    public PedidoDTO(Pedido pedido, List<DetalleDTO> pedidosDTOS) {
        this.id = pedido.getId_pedido();
        this.usuarioId = pedido.getUsuario().get_id();
        this.direccionId = pedido.getDireccionEnvio().getDireccion_id();
        this.nombreUsuario = pedido.getUsuario().getNombre()+" "+pedido.getUsuario().getApellido();
        this.email = pedido.getUsuario().getEmail();
        this.precioTotal = pedido.getTotalPrecio();
        this.totalProductos = pedido.getDetalles().size();
        this.estadoPedido = pedido.getEstadoPedido();
        this.metodoPago = pedido.getMetodoPago();
        this.listaDetalles = pedidosDTOS;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public long getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(long direccionId) {
        this.direccionId = direccionId;
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
}
