package com.mol21.Service_DeliveryRice.controller;

import com.mol21.Service_DeliveryRice.model.Usuario;
import com.mol21.Service_DeliveryRice.service.UsuarioService;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public GenericResponse<Usuario> login(HttpServletRequest request){
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        return usuarioService.login(email, password);
    }
    @PostMapping()
    public GenericResponse<Usuario> registerUser(@RequestBody Usuario u){
        System.out.println("Nombre recibido: " + u.getNombre()); // Debug
        return this.usuarioService.registrarUsuario(u);
    }
    @PutMapping("/{id}")
    public GenericResponse<Usuario> updateUser(@PathVariable long id, @RequestBody Usuario u){
        return this.usuarioService.updateUsuario(id,u);
    }

}
