package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.DTO.DireccionDTO;
import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.model.Usuario;
import com.mol21.Service_DeliveryRice.persistence.DireccionRepository;
import com.mol21.Service_DeliveryRice.persistence.PedidoRepository;
import com.mol21.Service_DeliveryRice.persistence.UsuarioRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import com.mol21.Service_DeliveryRice.utils.Global;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DireccionService {
    private final DireccionRepository repository;
    private final UsuarioRepository usuRepository;

    public DireccionService(DireccionRepository repository, UsuarioRepository usuRepository) {
        this.repository = repository;
        this.usuRepository = usuRepository;
    }

    //1.-) Obtener direcciones de un usuario
    public GenericResponse<List<DireccionDTO>> obtenerDirecciones(long usuarioId) {
        List<Direccion> direcciones = repository.findByUsuario_id(usuarioId);
        //System.out.println(usuRepository.findEmailById(usuarioId));
        if (!direcciones.isEmpty()) {
            List<DireccionDTO> direccionesDTO = new ArrayList<>();
            for(Direccion direccion : direcciones){
                DireccionDTO dto = new DireccionDTO(direccion);
                dto.setEmailUsuario(usuRepository.findEmailById(usuarioId));
                direccionesDTO.add(dto);
            }
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_OK,
                    "Se ha obtenido la lista de las direcciones del usuario " + usuarioId,
                    direccionesDTO
            );
        } else {
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_WARNING,
                    "El usuario introducido no tiene ninguna direccion activa",
                    null
            );
        }

    }

    //Obtener la direccion principal de un usuario
    public GenericResponse<DireccionDTO> getPrincipal(long idUsuario) {
        Optional<Direccion> optD = repository.findByUsuario_idAndEsPrincipalTrue(idUsuario);
        if (optD.isPresent()) {
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_OK,
                    "Se ha devuelto la direccion principal del usuario",
                    new DireccionDTO(optD.get())
            );
        } else {
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_WARNING,
                    "No se ha encontrado direccion principal para ese usuario",
                    null
            );
        }
    }

    // 2.-) Marcar una direccion como principal

    public GenericResponse<DireccionDTO> setDireccionPrincipal(long idDireccion) {
        Optional<Direccion> optDireccion = repository.findById(idDireccion);

        if (!optDireccion.isPresent()) {
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_WARNING,
                    "La direccion introducida no es correcta",
                    null
            );
        } else {
            Direccion direccionTemporal = (Direccion) optDireccion.get();

            //Eliminamos atributo a la direccion principal
            repository.resetPrincipal(direccionTemporal.getUsuario().get_id());

            //Marcamos la direccion como Principal
            direccionTemporal.setEsPrincipal(true);

            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_OK,
                    "dirección establecida como principal",
                    new DireccionDTO(repository.save(direccionTemporal))
            );
        }
    }

    //3.-) Agregar una nueva direccion
    public GenericResponse<DireccionDTO> guardarDireccion(long usuarioId, Direccion d) {
        Optional<Usuario> optU = usuRepository.findById(usuarioId);

        //Comprobamos que el usuario que se hOba dado por parametro existe, buscamos en su repositorio
        if (!optU.isPresent()) {
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_WARNING,
                    "El usuario asociado no existe",
                    null
            );
        } else {
            //Le asignamos ese usuario a la direccion
            d.setUsuario(optU.get());
        }

        Optional<Direccion> optD = repository.findByUsuarioAndCalleAndNumeroAndCiudadAndCodPostal(
                d.getUsuario(), d.getCalle(), d.getNumero(), d.getCiudad(), d.getCodPostal());

        //Comprobamos que la direccion existe y si no, creamos una nueva
        if (optD.isPresent()) {
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_WARNING,
                    "El usuario ya tiene esa direccion registrada",
                    null
            );
        } else {
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_OK,
                    "Se ha guardado la direccion con exito",
                    new DireccionDTO(repository.save(d))
            );
        }
    }


    //4.-) Modificar una direccion
    public GenericResponse<DireccionDTO> modificarDireccion(Direccion d) {
        Optional<Direccion> optD = repository.findById(d.getDireccion_id());

        if (optD.isPresent()) {
            Usuario usuarioDireccion = optD.get().getUsuario();

            if(d.isEsPrincipal()){
                repository.resetPrincipal(usuarioDireccion.get_id());
            }

            if (d.getUsuario() == null) {
                d.setUsuario(usuarioDireccion);
            }
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_OK,
                    "Direccion actualizada correctamente",
                    new DireccionDTO(repository.save(d))
            );
        } else {
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_WARNING,
                    "Esa direccion no existe",
                    null
            );
        }
    }

    //5.-) Eliminar una direccion
    public GenericResponse<Void> eliminarDireccion(long id) {
        Optional<Direccion> optD = repository.findById(id);
        if (optD.isPresent()) {
            repository.delete(optD.get());
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_OK,
                    "Direccion eliminada",
                    null
            );
        } else {
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_WARNING,
                    "No se ha encontrado la direccion a eliminar",
                    null
            );
        }
    }

    //6.-)Eliminar varias direcciones
    public GenericResponse<Void> eliminarDirecciones(List<Long> ids) {
        List<Long> eliminadas = new ArrayList<>();
        List<Long> noEncontradas = new ArrayList<>();
        for (Long id : ids) {
            Optional<Direccion> optD = repository.findById(id);
            if (optD.isPresent()) {
                repository.delete(optD.get());
                 eliminadas.add(id);
            } else {
                noEncontradas.add(id);
            }
        }

        if(noEncontradas.isEmpty()){
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_OK,
                    "Direcciones eliminadas",
                    null
            );
        }else if(eliminadas.isEmpty()){
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_WARNING,
                    "No se han encontrado las direcciones a eliminar",
                    null
            );
        }else{
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_WARNING,
                    "Algunas direcciones no fueron encontradas"+ noEncontradas,
                    null
            );
        }
    }

}









