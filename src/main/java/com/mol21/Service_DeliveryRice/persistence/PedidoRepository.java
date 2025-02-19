package com.mol21.Service_DeliveryRice.persistence;

import com.mol21.Service_DeliveryRice.model.Pedido;
import com.mol21.Service_DeliveryRice.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PedidoRepository  extends CrudRepository<Pedido, Long> {
    List<Pedido> findAllByUsuario(Usuario usuario);
}
