package com.mol21.Service_DeliveryRice.model.DTO;

import com.mol21.Service_DeliveryRice.model.Carrito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CarritoDTO {
    private long id;
    private BigDecimal precioCarrito;
    private int totalProductos;
    private List<ItemDTO> listaItems;
    private boolean esProcesado;
    public CarritoDTO(Carrito carrito, ArrayList<ItemDTO> listaDTOS) {
        this.id = carrito.getCarrito_id();
        this.totalProductos = carrito.getTotalProductos();
        this.precioCarrito = carrito.getTotalPrecio();
        this.esProcesado = carrito.isProcesado();
        this.listaItems = listaDTOS;
    }

    public CarritoDTO(Carrito carrito) {
        this.id = carrito.getCarrito_id();
        this.totalProductos = carrito.getTotalProductos();
        this.precioCarrito = carrito.getTotalPrecio();
        this.esProcesado = carrito.isProcesado();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getPrecioCarrito() {
        return precioCarrito;
    }

    public void setPrecioCarrito(BigDecimal precioCarrito) {
        this.precioCarrito = precioCarrito;
    }

    public int getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(int totalProductos) {
        this.totalProductos = totalProductos;
    }

    public List<ItemDTO> getListaItems() {
        return listaItems;
    }

    public void setListaItems(List<ItemDTO> listaItems) {
        this.listaItems = listaItems;
    }

    public boolean isEsProcesado() {
        return esProcesado;
    }

    public void setEsProcesado(boolean esProcesado) {
        this.esProcesado = esProcesado;
    }
}
