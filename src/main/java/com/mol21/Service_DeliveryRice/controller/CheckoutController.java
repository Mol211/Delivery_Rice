package com.mol21.Service_DeliveryRice.controller;

import com.mol21.Service_DeliveryRice.model.DTO.PedidoDTOAdmin;
import com.mol21.Service_DeliveryRice.model.EstadoPedido;
import com.mol21.Service_DeliveryRice.persistence.PedidoRepository;
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
    public GenericResponse<List<PedidoDTOAdmin>> obtenerPedidos(
            @RequestParam(value = "idUsuario", required = false) Long idUsuario,
            @RequestParam(value = "idRepartidor", required = false) Long idRepartidor,
            @RequestParam(value = "estadp", required = false) EstadoPedido estado) {
        return checkoutService.obtenerPedidos(idUsuario, idRepartidor, estado);
    }
    @GetMapping("/pedido")
    public GenericResponse<PedidoDTOAdmin> obtenerUnPedido(@PathVariable ("idPedido") long idPedido){
        return checkoutService.obtenerPedido(idPedido);
    }
    @PutMapping()

}


    //Asignar un pedido a un repartidor

//}
