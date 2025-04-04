package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.DTO.RegistrarUsuarioDTO;
import com.mol21.Service_DeliveryRice.model.DTO.UsuarioDTO;
import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.model.Rol;
import com.mol21.Service_DeliveryRice.model.Usuario;
import com.mol21.Service_DeliveryRice.persistence.DireccionRepository;
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

    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, DireccionRepository direccionRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository
        ;
        this.direccionRepository = direccionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //El administrador debe tener un metodo para listar usuarios y otro para clientes
    //Registrar un usuario
    private GenericResponse<UsuarioDTO> registrarUsuario(RegistrarUsuarioDTO regUsuario, Rol rol) {
        if (usuarioRepository.existsByEmail(regUsuario.getU().getEmail())) {
            return new GenericResponse<>(
                    Global.TIPO_AUTH,
                    Global.RPTA_WARNING,
                    "El usuario ya está registrado",
                    null
            );
        }
        return crearUsuario(regUsuario, rol);
    }
    //Registramos un Cliente
    public GenericResponse<UsuarioDTO> registrarCliente(RegistrarUsuarioDTO registrarUsuarioDTO) {
        return registrarUsuario(registrarUsuarioDTO, CLIENTE);
    }
    //Registramos un Repartidor
    public GenericResponse<UsuarioDTO> registrarRepartidor(RegistrarUsuarioDTO registrarUsuarioDTO) {
        return registrarUsuario(registrarUsuarioDTO, REPARTIDOR);
    }

    //Crear un usuario si no existe el Email en la BD
    public GenericResponse<UsuarioDTO> crearUsuario(RegistrarUsuarioDTO regUsuario, Rol rol) {
        if (usuarioRepository.existsByEmail(regUsuario.getU().getEmail())) {
            return new GenericResponse<>(
                    Global.TIPO_AUTH,
                    Global.RPTA_WARNING,
                    "Ya existe un usuario con ese correo, introduzca uno válido",
                    null
            );
        } else {

            regUsuario.getU().setRol(rol);
            regUsuario.getU().setPassword(passwordEncoder.encode(regUsuario.getU().getPassword()));
            UsuarioDTO usuarioDto = new UsuarioDTO(usuarioRepository.save(regUsuario.getU()));
            regUsuario.getD().setUsuario(regUsuario.getU());
            direccionRepository.save(regUsuario.getD());
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_OK,
                    "Se ha creado un nuevo usuario",
                    usuarioDto);
        }


    }

   //Login de usuario
    public GenericResponse<UsuarioDTO> login(String email, String password) {
        Optional<Usuario> optU = usuarioRepository.findByEmail(email);
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
        Optional<Usuario> optU = usuarioRepository.findById(id);
        if (optU.isPresent()) {
            Usuario usuarioExistente = optU.get();
            if (usuarioRepository.existsByEmail(u.getEmail()) && (!u.getEmail().equals(usuarioExistente.getEmail()))) {
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
                        new UsuarioDTO(usuarioRepository.save(usuarioExistente))
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




