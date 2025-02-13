package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.CategoriaProducto;
import com.mol21.Service_DeliveryRice.model.DTO.ProductoDTO;
import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.model.Producto;
import com.mol21.Service_DeliveryRice.persistence.ProductoRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import static com.mol21.Service_DeliveryRice.utils.Global.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {
    private final ProductoRepository repository;
    public ProductoService(ProductoRepository repository){
        this.repository = repository;
    }



    //Obtener un producto
    public GenericResponse<ProductoDTO> obtenerProducto(long id) {
        Optional<Producto> producto =repository.findById(id);
        if(!producto.isPresent()){
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No se han encontrado productos en esa categoria",
                    null
            );
        } else{
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_OK,
                    "Se ha devuelvo el producto",
                    new ProductoDTO(producto.get())
            );
        }
    }

    //1.- Obtener Producto por Categoria
    public GenericResponse<List> getProductByCategory(CategoriaProducto categoria){
        List<Producto> productos = repository.findByCategoriaProducto(categoria);
        List<ProductoDTO> productosDTOS = new ArrayList<>();
        for(Producto prod : productos){
            productosDTOS.add(new ProductoDTO(prod));
        }
        if(productos.isEmpty()){
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No se han encontrado productos en esa categoria",
                    null
            );
        } else {
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_OK,
                    "Se han obtenido la lista de productos por categoria",
                    productosDTOS
            );
        }
    }


    //2.- Guardar un Producto
    public GenericResponse<ProductoDTO> registrarProducto(Producto p){
        Optional<Producto> optP = repository.findById(p.getId_product());
        if(optP.isPresent()){
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "Ese producto ya existe",
                    null
            );
        } else {
            return new GenericResponse<>(
              TIPO_DATA,
              RPTA_OK,
              "Producto ingresado con éxito",
              new ProductoDTO(repository.save(p))
            );
        }
    }

    //3.- Modificar Producto
    public GenericResponse<ProductoDTO> modificarProducto(Producto p){
        Optional<Producto> optP =repository.findById(p.getId_product());
        if(!optP.isPresent()){
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No se ha encontrado ese producto",
                    null
            );
        } else {
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_OK,
                    "Producto modifcado con éxito",
                    new ProductoDTO(repository.save(p))
            );
        }
    }

    //4.-Eliminar Producto
    public GenericResponse<Void> eliminarProducto(long id){
        Optional<Producto> optP = repository.findById(id);
        if(optP.isPresent()){
            repository.delete(optP.get());
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_OK,
                    "Producto eliminado con éxito",
                    null
            );
        } else {
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No se encuentra el producto",
                    null
            );
        }
    }

}
