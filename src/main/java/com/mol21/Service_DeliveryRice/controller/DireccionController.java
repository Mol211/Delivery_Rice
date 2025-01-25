package com.mol21.Service_DeliveryRice.controller;

import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.service.DireccionService;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/direccion")
public class DireccionController {

    private final DireccionService service;
    public DireccionController(DireccionService service){
        this.service = service;
    }
    //Endpoing para que el cliente pueda obtener una lista de direcciones
    @GetMapping("/{idUsuario}")
    public GenericResponse<List<Direccion>> obtenerDirecciones(@PathVariable long idUsuario){
        return service.obtenerDirecciones(idUsuario);
    }

    //Endpoing para que el cliente pueda marcar una direccion como principal
    @PutMapping("/principal")
    public GenericResponse<Direccion>hacerPrincipal(@RequestBody Direccion d){
        return service.setDireccionPrincipal(d);
    }



}
