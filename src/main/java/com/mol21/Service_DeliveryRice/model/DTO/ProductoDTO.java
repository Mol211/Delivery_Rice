package com.mol21.Service_DeliveryRice.model.DTO;

import com.mol21.Service_DeliveryRice.model.CategoriaProducto;
import com.mol21.Service_DeliveryRice.model.Producto;

import java.math.BigDecimal;

public class ProductoDTO {
    private long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private CategoriaProducto categoria;
    private String imagenURL;

    public ProductoDTO(Producto producto){
        this.id = producto.getId_product();
        this.nombre = producto.getNombre();
        this.descripcion = producto.getDescripcion();
        this.precio = producto.getPrecio();
        this.stock = producto.getStock();
        this.categoria = producto.getCategoriaProducto();
        this.imagenURL = producto.getImagenUrl();
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public int getStock() {
        return stock;
    }

    public long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public String getNombre() {
        return nombre;
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

