package com.mol21.Service_DeliveryRice.controller;

import com.mol21.Service_DeliveryRice.model.DTO.RegistrarUsuarioDTO;
import com.mol21.Service_DeliveryRice.model.DTO.UsuarioDTO;
import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.model.Usuario;
import com.mol21.Service_DeliveryRice.service.UsuarioService;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public GenericResponse<UsuarioDTO> login(HttpServletRequest request){
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        return usuarioService.login(email, password);
    }
    @PostMapping("/cliente")
    public GenericResponse<UsuarioDTO> registrarCliente(@RequestBody RegistrarUsuarioDTO regUsuarioDTO){
        return this.usuarioService.registrarCliente(regUsuarioDTO);
    }


    @PostMapping("/repartidor")
    public GenericResponse<UsuarioDTO> registrarRepartidor(@RequestBody RegistrarUsuarioDTO regUsuarioDTO){
        return usuarioService.registrarRepartidor(regUsuarioDTO);
    }

    @PutMapping("/{id}")
    public GenericResponse<UsuarioDTO> updateUser(@PathVariable long id, @RequestBody Usuario u){
        return this.usuarioService.modificarCliente(id,u);
    }


}
