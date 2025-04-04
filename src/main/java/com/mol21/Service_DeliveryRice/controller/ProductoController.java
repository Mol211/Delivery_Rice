package com.mol21.Service_DeliveryRice.controller;

import com.mol21.Service_DeliveryRice.model.CategoriaProducto;
import com.mol21.Service_DeliveryRice.model.DTO.ProductoDTO;
import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.model.Producto;
import com.mol21.Service_DeliveryRice.service.ProductoService;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.mol21.Service_DeliveryRice.utils.Global.RPTA_ERROR;
import static com.mol21.Service_DeliveryRice.utils.Global.TIPO_AUTH;

@RestController
@RequestMapping("api/producto")
public class ProductoController {

    private final ProductoService service;
    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public GenericResponse<ProductoDTO> obtenerProducto(@PathVariable long id){
        return service.obtenerProducto(id);
    }

    @GetMapping("/categoria/{categoria}")
    public GenericResponse<List>obtenerProductos(@PathVariable CategoriaProducto categoria){
        return service.getProductByCategory(categoria);
    }

    @PostMapping()
    public GenericResponse<ProductoDTO> registrarProducto(@RequestBody Producto p){
        System.out.println("El id del producto es " +p.getId_product());
        return service.registrarProducto(p);
    }


    @PatchMapping("/{id}")
    public GenericResponse<ProductoDTO> modificarProducto
            (@PathVariable long id, @RequestBody Map<String, Object> updates){

        return service.modificarProducto(id, updates);
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> eliminarProducto(@PathVariable long id){
        return service.eliminarProducto(id);
    }


}
