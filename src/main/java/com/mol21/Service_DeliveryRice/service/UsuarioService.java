package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.DTO.UsuarioDTO;
import com.mol21.Service_DeliveryRice.model.Rol;
import com.mol21.Service_DeliveryRice.model.Usuario;
import com.mol21.Service_DeliveryRice.persistence.UsuarioRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mol21.Service_DeliveryRice.utils.Global;
import java.util.Optional;

import static com.mol21.Service_DeliveryRice.model.Rol.*;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    //Registrar un usuario
    private GenericResponse<UsuarioDTO> registrarUsuario(Usuario u, Rol rol) {
        if (repository.existsByEmail(u.getEmail())) {
            return new GenericResponse<>(
                    Global.TIPO_AUTH,
                    Global.RPTA_WARNING,
                    "El usuario ya está registrado",
                    null
            );
        }
        return crearUsuario(u, rol);
    }
    //Registramos un Cliente
    public GenericResponse<UsuarioDTO> registrarCliente(Usuario u) {
        return registrarUsuario(u, CLIENTE);
    }
    //Registramos un Repartidor
    public GenericResponse<UsuarioDTO> registrarRepartidor(Usuario u) {
        return registrarUsuario(u, REPARTIDOR);
    }

    //Crear un usuario si no existe el Email en la BD
    public GenericResponse<UsuarioDTO> crearUsuario(Usuario u, Rol rol) {
        if (repository.existsByEmail(u.getEmail())) {
            return new GenericResponse<>(
                    Global.TIPO_AUTH,
                    Global.RPTA_WARNING,
                    "Ya existe un usuario con ese correo, introduzca uno válido",
                    null
            );
        } else {
            u.setRol(rol);
            u.setPassword(passwordEncoder.encode(u.getPassword()));
            UsuarioDTO usuarioDto = new UsuarioDTO(repository.save(u));
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_OK,
                    "Se ha creado un nuevo usuario",
                    usuarioDto);
        }


    }

   //Login de usuario
    public GenericResponse<UsuarioDTO> login(String email, String password) {
        Optional<Usuario> optU = repository.findByEmail(email);
        //Optional<Usuario> usuarioOptional = repository.login(email, password);
        if (optU.isPresent()) {
            //Si usuario existe se instancia
            Usuario u = optU.get();
            String passwordT = optU.get().getPassword();
            UsuarioDTO usuarioDto = new UsuarioDTO(u);
            System.out.println("Contraseña ingresada: " + password);
            System.out.println("Hash de la BD: " + passwordT);
            if (passwordEncoder.matches(password, passwordT)) {
            //if (password.equals(usuario.getPassword())) {
                return new GenericResponse<>(
                        Global.TIPO_AUTH,
                        Global.RPTA_OK,
                        "Has iniciado sesión correctamente",
                        usuarioDto
                );
            } else {
                return new GenericResponse<>(
                        Global.TIPO_AUTH,
                        Global.RPTA_WARNING,
                        "Contraseña incorrecta",
                        null
                );
            }
        }
        else {
            return new GenericResponse<>(
                    Global.TIPO_AUTH,
                    Global.RPTA_WARNING,
                    "Usuario no encontrado",
                    null
            );
        }
    }

    //El cliente puede modificar sus datos
    public GenericResponse<UsuarioDTO> modificarCliente(long id, Usuario u) {
        Optional<Usuario> optU = repository.findById(id);
        if (optU.isPresent()) {
            Usuario usuarioExistente = optU.get();
            if (repository.existsByEmail(u.getEmail()) && (!u.getEmail().equals(usuarioExistente.getEmail()))) {
                return new GenericResponse<>(
                        Global.TIPO_AUTH,
                        Global.RPTA_WARNING,
                        "Ya existe un usuario con ese correo, introduzca uno válido",
                        null
                );
            }
            else {
                //Obtenemos usuario de BD

                usuarioExistente.setEmail(u.getEmail());
                usuarioExistente.setPassword(passwordEncoder.encode(u.getPassword()));
                usuarioExistente.setNombre(u.getNombre());
                usuarioExistente.setApellido(u.getApellido());
                usuarioExistente.setTelefono(u.getTelefono());
                return new GenericResponse<>(
                        Global.TIPO_AUTH,
                        Global.RPTA_OK,
                        "Se han actualizado los datos del usuario " + usuarioExistente.get_id(),
                        new UsuarioDTO(repository.save(usuarioExistente))
                );
            }

        }
        else {
            return new GenericResponse<>(
                    Global.TIPO_AUTH,
                    Global.RPTA_WARNING,
                    "No se encuentra el usuario",
                    null
            );
        }
    }
}




