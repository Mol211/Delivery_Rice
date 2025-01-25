package com.mol21.Service_DeliveryRice.model;

import com.mol21.Service_DeliveryRice.persistence.UsuarioRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import com.mol21.Service_DeliveryRice.utils.Global;

import java.util.Optional;

public class registrarUsuario {
    private final UsuarioRepository repository;

    public registrarUsuario(UsuarioRepository repository) {
        this.repository = repository;
    }

    public GenericResponse<Usuario> registrar(Usuario u) {
        //nos dan un usuario y queremos encontrarlo en la BD
        Optional<Usuario> optU = repository.findById(u.get_id());
        //Si ese usuario no existe:
        // buscamos si existe un email en la BD
        //Si no existe, creamos un usuario
        if (!optU.isPresent()) {
            if (repository.existsByEmail(optU.get().getEmail())) {
                return new GenericResponse<>(
                        Global.TIPO_AUTH,
                        Global.RPTA_WARNING,
                        "Ya existe un usuario con ese correo, introduzca uno válido",
                        optU.get()
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
                    Global.RPTA_OK,
                    "Se ha actualizado el usuario",
                    repository.save(u)
            );
        }
    }
}

