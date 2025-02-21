package com.mol21.Service_DeliveryRice.persistence;

import com.mol21.Service_DeliveryRice.model.EstadoPedido;
import com.mol21.Service_DeliveryRice.model.Pedido;
import com.mol21.Service_DeliveryRice.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PedidoRepository  extends CrudRepository<Pedido, Long> {
    List<Pedido> findAllByUsuario(Usuario usuario);

    List<Pedido> findAllByRepartidorAndEstadoPedido(Usuario repartidor, EstadoPedido estadoPedido);

    void findAllByEstadoPedidoNotCompletado();

    List<Pedido> findAllByEstadoPedido(EstadoPedido estadoPedido);

    List<Pedido> findAllByUsuarioAndEstadoPedido(Usuario usuario, EstadoPedido estadoPedido);

    List<Pedido> findAllByRepartidor(Usuario usuario);

    List<Pedido> findAllByUsuarioAndRepartidorAndEstadoPedido(Usuario usuario, Usuario usuario1, EstadoPedido estadoPedido);
}
