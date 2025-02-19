package com.mol21.Service_DeliveryRice.controller;

import com.mol21.Service_DeliveryRice.model.DTO.CarritoDTO;
import com.mol21.Service_DeliveryRice.model.DTO.ItemDTO;
import com.mol21.Service_DeliveryRice.model.DTO.ProductoDTO;
import com.mol21.Service_DeliveryRice.model.Producto;
import com.mol21.Service_DeliveryRice.service.ItemService;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/item")
public class ItemController {
    private final ItemService service;


    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping("/agregar")
    public GenericResponse<CarritoDTO> addItem(@RequestParam long idCarrito, @RequestParam long idProducto, @RequestParam int cantidad){
        return service.addItemsToCarrito(idCarrito, idProducto, cantidad);
    }

    @PutMapping("/modificar/{itemId}")
    public GenericResponse<CarritoDTO> modificarItem(@PathVariable long itemId, @RequestParam int cantidad
    ){
        return service.modificarCantidad( itemId,  cantidad);
    }

    @GetMapping("listaItems/{carritoId}")
    public GenericResponse<List<ItemDTO>> listaItems(@PathVariable long carritoId){
        return service.getItemsByCarrito(carritoId);
    }

    @DeleteMapping("eliminar-item/{idItem}")
    public GenericResponse<CarritoDTO> eliminarItem(@PathVariable long idItem){
        return service.eliminarItem(idItem);
    }


}
