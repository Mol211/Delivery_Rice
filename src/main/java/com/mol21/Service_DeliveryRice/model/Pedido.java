package com.mol21.Service_DeliveryRice.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mol21.Service_DeliveryRice.model.EstadoPedido.PENDIENTE;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estadoPedido;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrecio;

    @Column(nullable = false)
    private int totalDetalles;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetallePedido> detalles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "direccion_envio_id", nullable = false)
    private Direccion direccionEnvio;

    @OneToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito; // Relación con Carrito

    @ManyToOne
    @JoinColumn(name = "repartidor_id", referencedColumnName = "id") // Repartidor asignado al pedido
    private Usuario repartidor;


    public Pedido(Carrito carrito, Direccion direccion, MetodoPago metodoPago) {
        this.carrito=carrito;
        this.direccionEnvio = direccion;
        this.metodoPago = metodoPago;
        this.estadoPedido = PENDIENTE;
        this.usuario = carrito.getUsuario();
        this.fechaCreacion = LocalDateTime.now();
        this.totalPrecio = carrito.getTotalPrecio();
    }

    public long getId_pedido() {
        return id;
    }

    public void setId_pedido(long pedido_id) {
        this.id = pedido_id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public BigDecimal getTotalPrecio() {
        return totalPrecio;
    }

    public void setTotalPrecio(BigDecimal totalPrecio) {
        this.totalPrecio = totalPrecio;
    }

    public int getTotalDetalles() {
        return totalDetalles;
    }

    public void setTotalDetalles(int totalDetalles) {
        this.totalDetalles = totalDetalles;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Direccion getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(Direccion direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }
    public Usuario getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Usuario repartidor) {
        this.repartidor = repartidor;
    }


}
