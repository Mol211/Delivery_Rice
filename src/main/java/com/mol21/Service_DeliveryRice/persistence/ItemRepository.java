package com.mol21.Service_DeliveryRice.persistence;

import com.mol21.Service_DeliveryRice.model.Carrito;
import com.mol21.Service_DeliveryRice.model.ItemCarrito;
import com.mol21.Service_DeliveryRice.model.Producto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

//Por qué no se envia el carritoID.
public interface ItemRepository extends CrudRepository<ItemCarrito, Long> {

    //Listar Items de un Carrito
    List<ItemCarrito> findByCarrito(Carrito carrito);

    //Eliminar Items de un carrito
    void deleteByCarrito(Carrito carrito);

    Optional<ItemCarrito> findByCarritoAndProducto(Carrito carrito, Producto producto);
}
