package com.mol21.Service_DeliveryRice.persistence;

import com.mol21.Service_DeliveryRice.model.CategoriaProducto;
import com.mol21.Service_DeliveryRice.model.Producto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductoRepository extends CrudRepository <Producto, Long>{
    List<Producto> findByCategoriaProducto(CategoriaProducto categoria);
}
