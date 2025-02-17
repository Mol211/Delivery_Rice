package com.mol21.Service_DeliveryRice.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false)
    private int cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subTotal;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public ItemCarrito(int cantidad, Carrito carrito, Producto producto) {
        this.cantidad=cantidad;
        this.producto= producto;
        this.carrito = carrito;
        this.subTotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }

    public ItemCarrito() {

    }

    public long getItem_id() {
        return id;
    }

    public void setItem_id(long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {

        this.cantidad = cantidad;
        this.subTotal= producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
