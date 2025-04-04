package com.mol21.Service_DeliveryRice.model.DTO;

import com.mol21.Service_DeliveryRice.model.DetallePedido;

import java.math.BigDecimal;

public class DetalleDTO {
    //Información del DetalleDTO
    private long id;
    private int cantidad;
    private BigDecimal subTotal;

    //Información del Producto
    private long idProducto;
    private String nombreProducto;
    private BigDecimal precioUnitario;
    private String descripcionProducto;
    private String imagen;



    public DetalleDTO (DetallePedido detalle){
        this.id = detalle.getId();
        this.nombreProducto = detalle.getProducto().getNombre();
        this.cantidad = detalle.getCantidad();
        this.subTotal = detalle.getSubTotal();
        this.precioUnitario = detalle.getPrecioUnitario();
        this.idProducto = detalle.getProducto().getId_product();
        this.descripcionProducto = detalle.getProducto().getDescripcion();
        this.imagen = detalle.getProducto().getImagenUrl();
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

}
