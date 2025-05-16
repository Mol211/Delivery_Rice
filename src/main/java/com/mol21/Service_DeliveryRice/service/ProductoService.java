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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                    "No se encuentra el producto",
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
        System.out.println(p.getNombre());
        System.out.println(p.getCategoriaProducto());
        System.out.println(p.getStock());
        System.out.println(p.getDescripcion());
        System.out.println(p.getImagenUrl());
        System.out.println(p.getPrecio());
        Optional<Producto> optP = repository.findByNombre(p.getNombre());
        if(optP.isPresent()){
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "Ese producto ya existe",
                    null
            );
        }
        else {
            repository.save(p);
            ProductoDTO pDTO = new ProductoDTO(p);
            return new GenericResponse<>(
              TIPO_DATA,
              RPTA_OK,
              "Producto ingresado con éxito",
              pDTO
            );
        }
    }

    //3.- Modificar Producto
    public GenericResponse<ProductoDTO> modificarProducto(long id, Map<String,Object> updates){
        Optional<Producto> optP =repository.findById(id);
        if(!optP.isPresent()){
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No se encuentra el producto",
                    null
            );
        } else {
            Producto p = optP.get();
            try{
                updates.forEach((campo,valor) ->{
                    switch (campo){
                        case "nombre":
                            p.setNombre((String) valor);
                            break;
                        case"stock":
                            p.setStock((Integer) valor);
                            break;
                        case"categoria_producto":
                            p.setCategoriaProducto((CategoriaProducto) valor);
                            break;
                        case"descripcion":
                            p.setDescripcion((String) valor);
                            break;
                        case"imagen_url":
                            p.setImagenUrl((String) valor);
                            break;
                        case"precio":
                            p.setPrecio((BigDecimal) valor);
                            break;
                        default:
                            throw new IllegalArgumentException("Campo no válido");
                    }
                });
                repository.save(p);
                ProductoDTO pDTO = new ProductoDTO(p);
                return new GenericResponse<>(
                        TIPO_DATA,
                        RPTA_OK,
                        "Producto modifcado con éxito",
                        pDTO
                );
            }catch(IllegalArgumentException e){
                return new GenericResponse<>(
                        TIPO_EX,
                        RPTA_ERROR,
                        e.getMessage(),
                        null
                );
            }
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
