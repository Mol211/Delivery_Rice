package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.Usuario;
import com.mol21.Service_DeliveryRice.persistence.UsuarioRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mol21.Service_DeliveryRice.utils.Global;

import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public GenericResponse<Usuario> login(String email, String password) {
        Optional<Usuario> usuarioOptional = repository.login(email, password);
        if (usuarioOptional.isPresent()) {
            //Si usuario existe se instancia
            Usuario usuario = usuarioOptional.get();
            System.out.println("Contraseña ingresada: " + password);
            System.out.println("Hash de la BD: " + usuarioOptional.get().getPassword());
            //if (passwordEncoder.matches(password, usuario.getPassword())) {
            if (password.equals(usuario.getPassword())) {
                return new GenericResponse<Usuario>(
                        Global.TIPO_AUTH,
                        Global.RPTA_OK,
                        "Has iniciado sesión correctamente",
                        usuarioOptional.get()
                );
            } else {
                return new GenericResponse<Usuario>(
                        Global.TIPO_AUTH,
                        Global.RPTA_WARNING,
                        "Contraseña incorrecta",
                        new Usuario()
                );
            }
        } else {
            return new GenericResponse<Usuario>(
                    Global.TIPO_AUTH,
                    Global.RPTA_WARNING,
                    "Usuario no encontrado",
                    new Usuario()
            );
        }
    }

    public GenericResponse<Usuario> registrarUsuario(Usuario u) {
        //nos dan un usuario y queremos encontrarlo en la BD
        Optional<Usuario> optU = repository.findById(u.get_id());
        //Si ese usuario no existe:
        // buscamos si existe un email en la BD
        //Si no existe, creamos un usuario
        if (!optU.isPresent()) {
            if (repository.existsByEmail(u.getEmail())) {
                return new GenericResponse<>(
                        Global.TIPO_AUTH,
                        Global.RPTA_WARNING,
                        "Ya existe un usuario con ese correo, introduzca uno válido",
                        null
                );
            } else {
                return new GenericResponse<>(
                        Global.TIPO_DATA,
                        Global.RPTA_OK,
                        "Se ha creado un nuevo usuario",
                        repository.save(u)
                );
            }
            //Si el optional no está vacío, entonces actualiza los datos del usuario
        } else {
            return new GenericResponse<>(
                    Global.TIPO_AUTH,
                    Global.RPTA_WARNING,
                    "No se puede crear el usuario",
                    null
            );
        }
    }

    public GenericResponse<Usuario> updateUsuario(long id, Usuario u) {
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
            } else {
//Obtenemos usuario de BD

                usuarioExistente.setEmail(u.getEmail());
                usuarioExistente.setPassword(u.getPassword());
                usuarioExistente.setNombre(u.getNombre());
                usuarioExistente.setApellido(u.getApellido());
                usuarioExistente.setTelefono(u.getTelefono());

                return new GenericResponse<>(
                        Global.TIPO_AUTH,
                        Global.RPTA_OK,
                        "Se han actualizado los datos del usuario " + usuarioExistente.get_id(),
                        repository.save(usuarioExistente)
                );
            }

        } else {
            return new GenericResponse<>(
                    Global.TIPO_AUTH,
                    Global.RPTA_WARNING,
                    "No se encuentra el usuario",
                    null
            );
        }
    }
}
//    public GenericResponse<Usuario> registrarUsuario(Usuario u) {
//        Optional<Usuario> usuarioOptional = this.repository.findById(u.getUsuario_id());
//        if(repository.existsByEmail(usuarioOptional.get().getEmail()) &&
//                (usuarioOptional.isEmpty())||
//                !usuarioOptional.get().getEmail().equals(u.getEmail())) {
//            return new GenericResponse<>(
//                    Global.TIPO_DATA,
//                    Global.RPTA_WARNING,
//                    "Ese correo ya existe en un usuario",
//                    null
//            );
//        }
//        //Si el usuario existe ya en la base de datos idTemp = un numero, si nuevo usuario idTemp = 0
//        long idTemp = usuarioOptional.isPresent() ? usuarioOptional.get().getUsuario_id() : 0;
//        if(idTemp == 0){
//            //JPA detecta el ID y si no existe en la BD crea un objeto nuevo
//            return new GenericResponse<>(
//                    Global.TIPO_DATA,
//                    Global.RPTA_OK,
//                    "Usuario registrado correctamente",
//                    this.repository.save(u)
//            );
//        } else {
//            return new GenericResponse<>(
//                    Global.TIPO_DATA,
//                    Global.RPTA_OK,
//                    "Datos de usuario actualizados",
//                    this.repository.save(u)
//            );
//
//           }
//
//    }



