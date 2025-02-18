package com.mol21.Service_DeliveryRice.persistence;

import com.mol21.Service_DeliveryRice.model.Pedido;
import org.springframework.data.repository.CrudRepository;

public interface PedidoRepository  extends CrudRepository<Pedido, Long> {
}
