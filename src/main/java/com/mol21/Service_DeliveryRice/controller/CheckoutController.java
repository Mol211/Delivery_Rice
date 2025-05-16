package com.mol21.Service_DeliveryRice.controller;

import com.mol21.Service_DeliveryRice.model.DTO.PedidoDTO;
import com.mol21.Service_DeliveryRice.model.DTO.UsuarioDTO;
import com.mol21.Service_DeliveryRice.model.EstadoPedido;
import com.mol21.Service_DeliveryRice.service.CheckoutService;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Controlador de los estados del Pedido Administrador y Repartidor
@RestController
@RequestMapping("api/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    //Obtener todos los pedidos(permite filtrado)
    @GetMapping()
    public GenericResponse<List<PedidoDTO>> obtenerPedidos(
            @RequestParam(value = "idUsuario", required = false) Long idUsuario,
            @RequestParam(value = "idRepartidor", required = false) Long idRepartidor,
            @RequestParam(value = "estado", required = false) EstadoPedido estado) {
        return checkoutService.obtenerPedidos(idUsuario, idRepartidor, estado);
    }
    @GetMapping("/pedido/{idPedido}")
    public GenericResponse<PedidoDTO> obtenerUnPedido(@PathVariable ("idPedido") long idPedido){
        return checkoutService.obtenerPedido(idPedido);
    }
    @PutMapping("/cambio-estado/{idPedido}")
    public GenericResponse<PedidoDTO> cambiarEstado(
            @PathVariable ("idPedido") Long idPedido,
            @RequestParam (value = "accion", required = true) String accion,
            //preparar, enviar, cancelar, completar
            @RequestParam (value = "idRepartidor", required = false) Long idRepartidor){
        return checkoutService.cambiarEstado(idPedido,accion,idRepartidor);
    }
    @GetMapping("/repartidor")
    public GenericResponse<UsuarioDTO> obtenerRepartidorRandom(){
        return checkoutService.getUnRepartidor();
    }
}

