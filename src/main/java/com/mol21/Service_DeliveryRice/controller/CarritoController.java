package com.mol21.Service_DeliveryRice.controller;

import com.mol21.Service_DeliveryRice.model.DTO.CarritoDTO;
import com.mol21.Service_DeliveryRice.model.MetodoPago;
import com.mol21.Service_DeliveryRice.service.CarritoService;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/carrito")
public class CarritoController {
    private final CarritoService carritoService;
    public CarritoController(CarritoService carritoService){
        this.carritoService = carritoService;
    }
    @GetMapping("/{idUsuario}")
    public GenericResponse<CarritoDTO> obtenerCarrito(@PathVariable long idUsuario){
        return carritoService.obtenerCarrito(idUsuario);
    }
    @PostMapping("/usuario/{idUsuario}")
    public GenericResponse<CarritoDTO> nuevoCarrito(@PathVariable long idUsuario){
        return carritoService.nuevoCarrito(idUsuario);
    }
    @PostMapping("vaciar-carrito/usuario/{idUsuario}")
        public GenericResponse<CarritoDTO> vaciarCarrito(@PathVariable long idUsuario){
            return carritoService.vaciarCarrito(idUsuario);
    }
    @PostMapping("/procesar/{idCarrito}")
    public GenericResponse<Object> procesarCarrito(@PathVariable long idCarrito,
                                                   @RequestParam MetodoPago metodoPago,
                                                   @RequestParam long idDireccion){
        return carritoService.procesarPedido(idCarrito, metodoPago, idDireccion);
    }
}
