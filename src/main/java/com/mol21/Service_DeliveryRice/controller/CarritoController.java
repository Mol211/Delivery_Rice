package com.mol21.Service_DeliveryRice.controller;

import com.mol21.Service_DeliveryRice.model.Carrito;
import com.mol21.Service_DeliveryRice.model.DTO.CarritoDTO;
import com.mol21.Service_DeliveryRice.model.MetodoPago;
import com.mol21.Service_DeliveryRice.model.Usuario;
import com.mol21.Service_DeliveryRice.service.CarritoService;
import com.mol21.Service_DeliveryRice.service.ItemService;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.mol21.Service_DeliveryRice.utils.Global.*;

@RestController
@RequestMapping("api/carrito")
public class CarritoController {
    private final CarritoService carritoService;
    private final ItemService itemService;
    public CarritoController(CarritoService carritoService, ItemService itemService){
        this.carritoService = carritoService;

        this.itemService = itemService;
    }
    @GetMapping("/{id}")
    public GenericResponse<CarritoDTO> obtenerCarrito(@PathVariable long id){
        return carritoService.obtenerCarrito(id);
    }
    @PostMapping("/usuario/{idUsuario}")
    public GenericResponse<CarritoDTO> nuevoCarrito(@PathVariable long idUsuario){
        return carritoService.nuevoCarrito(idUsuario);
    }
    @PostMapping("vaciar-carrito/usuario/{idUsuario}")
        public GenericResponse<CarritoDTO> vaciarCarrito(@PathVariable long idUsuario){
            return carritoService.vaciarCarrito(idUsuario);
    }
    @GetMapping("/id-carrito/usuario/{idUsuario}")
    public GenericResponse<Long> obtenerIdCarritoSinProcesar(@PathVariable long idUsuario){
        return carritoService.obtenerIdCarritoSinProcesar(idUsuario);
    }
    @PostMapping("/procesar/{idCarrito}")
    public GenericResponse<Object> procesarCarrito(@PathVariable long idCarrito, @RequestParam MetodoPago metodoPago, @RequestParam long idDireccion){
        return carritoService.procesarPedido(idCarrito, metodoPago, idDireccion);
    }
}
