package com.mol21.Service_DeliveryRice.controller;

import com.mol21.Service_DeliveryRice.model.Carrito;
import com.mol21.Service_DeliveryRice.model.DTO.CarritoDTO;
import com.mol21.Service_DeliveryRice.model.MetodoPago;
import com.mol21.Service_DeliveryRice.model.Usuario;
import com.mol21.Service_DeliveryRice.service.CarritoService;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.mol21.Service_DeliveryRice.utils.Global.*;

@RestController
@RequestMapping("api/carrito")
public class CarritoController {
    private final CarritoService carritoService;
    public CarritoController(CarritoService carritoService){
        this.carritoService = carritoService;
    }
    @GetMapping("/{id}")
    public GenericResponse<CarritoDTO> obtenerCarrito(@PathVariable long id){
        return carritoService.obtenerCarrito(id);
    }
    @PostMapping("/{idUsuario}")
    public GenericResponse<CarritoDTO> nuevoCarrito(@PathVariable long idUsuario){
        return carritoService.nuevoCarrito(idUsuario);
    }
    @PostMapping("descartar/{idUsuario}")
    public GenericResponse<CarritoDTO> descartarCarrito(@PathVariable long idUsuario){
        return carritoService.descartarCarrito(idUsuario);
    }
    @GetMapping("/id-carrito/{idUsuario}")
    public GenericResponse<Long> obtenerIdCarritoSinProcesar(@PathVariable long idUsuario){
        return carritoService.obtenerIdCarritoSinProcesar(idUsuario);
    }
    @PostMapping("/procesar/{idCarrito}")
    public GenericResponse<Object> procesarCarrito(@PathVariable long idCarrito, @RequestParam MetodoPago metodoPago, @RequestParam long idDireccion){
        return carritoService.procesarPedido(idCarrito, metodoPago, idDireccion);
    }
}
