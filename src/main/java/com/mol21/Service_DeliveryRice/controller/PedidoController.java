package com.mol21.Service_DeliveryRice.controller;

import com.mol21.Service_DeliveryRice.model.DTO.PedidoDTOCliente;
import com.mol21.Service_DeliveryRice.service.PedidoService;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }
     @GetMapping("/cliente/{usuarioId}")
    public GenericResponse<List<PedidoDTOCliente>>obtenerPedidos(@PathVariable long usuarioId){
        return pedidoService.obtenerPedidosUsuario(usuarioId);
    }

    @GetMapping("/{pedidoId}")
    public GenericResponse<PedidoDTOCliente>obtenerPedido(@PathVariable long pedidoId){
        return pedidoService.obtenerPedidoId(pedidoId);
    }
}
