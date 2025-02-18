package com.mol21.Service_DeliveryRice.persistence;

import com.mol21.Service_DeliveryRice.model.DetallePedido;
import org.springframework.data.repository.CrudRepository;

public interface DetalleRepository extends CrudRepository<DetallePedido, Long> {
}
