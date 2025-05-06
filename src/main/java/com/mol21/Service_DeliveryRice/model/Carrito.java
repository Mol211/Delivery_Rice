package com.mol21.Service_DeliveryRice.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrecio;

    @Column(nullable = false)
    private int totalProductos;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemCarrito> items = new ArrayList<>();

    private boolean procesado ;
    //al pasar a true se creará el pedido

    @OneToOne(mappedBy = "carrito", cascade = CascadeType.ALL)
    private Pedido pedido; // Relación con Pedido

    public boolean isProcesado() {
        return procesado;
    }

    public void setProcesado(boolean procesado) {
        this.procesado = procesado;
    }

    public long getCarrito_id() {
        return id;
    }

    public void setCarrito_id(long id) {
        this.id = id;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getTotalPrecio() {
        return totalPrecio;
    }

    public void setTotalPrecio(BigDecimal totalPrecio) {
        this.totalPrecio = totalPrecio;
    }

    public int getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(int totalProductos) {
        this.totalProductos = totalProductos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ItemCarrito> Items() {
        return items;
    }

    public void setItemCarritoList(List<ItemCarrito> items) {
        this.items = items;
    }
    
}
