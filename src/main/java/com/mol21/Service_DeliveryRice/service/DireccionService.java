package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.persistence.DireccionRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import com.mol21.Service_DeliveryRice.utils.Global;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DireccionService {
    private final DireccionRepository repository;

    public DireccionService(DireccionRepository repository) {
        this.repository = repository;
    }

    //Obtener direcciones de un usuario
    public GenericResponse<List<Direccion>> obtenerDirecciones(long usuarioId) {
        return new GenericResponse<>(
                Global.TIPO_DATA,
                Global.RPTA_OK,
                "Se ha obtenido la lista de las direcciones del usuario "+usuarioId,
                repository.findByUsuario_id(usuarioId)
        );
    }

    //Marcar una direccion como principal
    public GenericResponse<Direccion> setDireccionPrincipal(Direccion d){
        Optional<Direccion> optDireccion = repository.findById(d.getDireccion_id());

        if(!optDireccion.isPresent()){
            return new GenericResponse<>(
                    Global.TIPO_DATA,
                    Global.RPTA_WARNING,
                    "La direccion introducida no es correcta",
                    null
            );
        }else {
            Direccion direccionTemporal = (Direccion) optDireccion.get();

            //Validamos que la direccion pertenece al usuario
            if(direccionTemporal.getUsuario().get_id() != d.getUsuario().get_id()){
                return new GenericResponse<>(
                        Global.TIPO_DATA,
                        Global.RPTA_WARNING,
                        "La direccion no pertenece al usuario",
                        null
                );
            }else{
                //Eliminamos atributo a la direccion principal
                repository.resetPrincipal(direccionTemporal.getUsuario().get_id());

                //Marcamos la direccion como Principal
                direccionTemporal.setEsPrincipal(true);

                return new GenericResponse<>(
                        Global.TIPO_DATA,
                        Global.RPTA_OK,
                        "dirección establecida como principal",
                        repository.save(direccionTemporal)
                );
            }
        }
    }




}




//    public GenericResponse<Direccion> guardarDireccion(Direccion d){
//        Optional<Direccion> dirOpt = repository.findById(d.getDireccion_id());
//        if(dirOpt.isPresent()){
//            return new GenericResponse<>{
//                Global.TIPO_DATA,
//                Global.RPTA_OK,
//                "Esa dirección "
//            };
//        }
//    }
//}

