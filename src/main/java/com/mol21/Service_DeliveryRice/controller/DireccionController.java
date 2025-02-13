package com.mol21.Service_DeliveryRice.controller;

import com.mol21.Service_DeliveryRice.model.DTO.DireccionDTO;
import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.service.DireccionService;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mol21.Service_DeliveryRice.utils.Global.*;

@RestController
@RequestMapping("api/direccion")
public class DireccionController {

    private final DireccionService service;
    public DireccionController(DireccionService service){

        this.service = service;
    }
    //Endpoing para que el cliente pueda obtener una lista de direcciones
    @GetMapping("/usuario/{idUsuario}")
    public GenericResponse<List<DireccionDTO>> obtenerDirecciones(@PathVariable long idUsuario){
        return service.obtenerDirecciones(idUsuario);
    }
    //Obtener la direccion principal del usuario
    @GetMapping("/principal/{idUsuario}")
    public GenericResponse<DireccionDTO> obtenerPrincipal(@PathVariable long idUsuario){
        return service.getPrincipal(idUsuario);
    }

    //Endpoing para que el cliente pueda marcar una direccion como principal
    @PutMapping("/principal/{id}")
    public GenericResponse<DireccionDTO>hacerPrincipal(@PathVariable Long id){
        return service.setDireccionPrincipal(id);
    }

    @PostMapping("/usuario/{idUsuario}")
    public GenericResponse<DireccionDTO> guardarDireccion(@PathVariable long idUsuario, @RequestBody Direccion d){
        return this.service.guardarDireccion(idUsuario, d);
    }

    @PutMapping("/{id}")
    public GenericResponse<DireccionDTO> actualizarDireccion(@PathVariable Long id, @RequestBody Direccion d){
        if(d.getDireccion_id()!=null && !d.getDireccion_id().equals(id)) {
            return new GenericResponse<>(TIPO_AUTH,
                    RPTA_ERROR,
                    "Los IDs de la URL y del cuerpo del JSON no coinciden",
                    null);
        }
        d.setDireccion_id(id);
        return service.modificarDireccion(d);
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> eliminarDireccion(@PathVariable Long id){
        return service.eliminarDireccion(id);
    }

    @DeleteMapping("/delete")
    public GenericResponse<Void> eliminarDirecciones(@RequestBody List<Long> ids){
        return service.eliminarDirecciones(ids);
    }



}
