package com.mol21.Service_DeliveryRice.persistence;

import com.mol21.Service_DeliveryRice.model.Carrito;
import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CarritoRepository extends CrudRepository<Carrito, Long> {

    Optional<Carrito> findByUsuarioAndEsProcesadoFalse(Usuario usuario);

}
