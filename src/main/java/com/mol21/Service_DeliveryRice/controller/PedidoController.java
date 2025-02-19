package com.mol21.Service_DeliveryRice.controller;

import com.mol21.Service_DeliveryRice.model.DTO.PedidoDTOAdmin;
import com.mol21.Service_DeliveryRice.service.PedidoService;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }
     @GetMapping()
    public GenericResponse<PedidoDTOAdmin>obtenerPedidos(){
        pedidoService.obtenerPedidos();
    }
}
