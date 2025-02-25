package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.*;
import com.mol21.Service_DeliveryRice.model.DTO.DetalleDTO;
import com.mol21.Service_DeliveryRice.model.DTO.PedidoDTOAdmin;
import com.mol21.Service_DeliveryRice.persistence.PedidoRepository;
import com.mol21.Service_DeliveryRice.persistence.UsuarioRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mol21.Service_DeliveryRice.model.EstadoPedido.*;
import static com.mol21.Service_DeliveryRice.utils.Global.*;
@Service
@Transactional
public class CheckoutService{
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    public CheckoutService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }
//Servicio que maneja el cambio de estados de los Pedidos Admin y Repartidor,
/*Métodos del Administrador*/
    //1.- Obtener Todos los Pedidos y filtrar por usuario, repartidor y estado
    //2.- Obtener un pedido en específico (idPedido)
    //3.- Asignar un repartidor al Pedido(idPedido/asignar-repartidor/idRepartidor) //ENVIAR PEDIDO
    //4.- ValidarPago (prepara el pedido)
    //5.- Cancelar un pedido
    //6.- Completar Pedido

    //1.- Obtener todos los pedidos (ADMIN)
    public GenericResponse<List<PedidoDTOAdmin>> obtenerPedidos(Long idUsuario, Long idRepartidor, EstadoPedido estadoPedido) {
        List<Pedido>listaPedidos;
        String mensaje;
        Usuario usuario = null;
        if(idUsuario != null){
            usuario = usuarioRepository.findById(idUsuario).orElse(null);
        }
        Usuario repartidor = null;
        if(idRepartidor != null){
            repartidor = usuarioRepository.findById(idRepartidor).orElse(null);
        }
        Optional<Usuario> optU = (idUsuario!=null) ? usuarioRepository.findById(idUsuario) : Optional.empty();
        Optional<Usuario> optR = (idRepartidor!=null) ? usuarioRepository.findById(idRepartidor) : Optional.empty();

        if(idUsuario != null && usuario == null){
            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se ha encontrado el usuario", null);
        }if(idRepartidor!=null && repartidor == null){
            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se ha encontrado el repartidor", null);
        }

        if(estadoPedido==null && idUsuario==null && idRepartidor==null){
            listaPedidos = (List<Pedido>) pedidoRepository.findAll();
            mensaje ="Se han obtenido todos los Pedidos";
        }
        else if(estadoPedido!=null && idUsuario==null && idRepartidor==null){
            listaPedidos = (List<Pedido>) pedidoRepository.findAllByEstadoPedido(estadoPedido);
            mensaje = "Se ha obtenido lista de Pedidos con el estado introducido";//+estadoPedido;

        }
        else if(estadoPedido!=null && idUsuario!=null && idRepartidor==null){
            listaPedidos = (List<Pedido>) pedidoRepository.findAllByUsuarioAndEstadoPedido(usuario,estadoPedido);
            mensaje = "Se ha obtenido lista de Pedidos del usuario con el estado introducido";
        }
        else if(estadoPedido!=null && idUsuario==null && idRepartidor!=null){
            listaPedidos = (List<Pedido>) pedidoRepository.findAllByRepartidorAndEstadoPedido(repartidor, estadoPedido);
            mensaje = "Se ha obtenido lista de Pedidos del repartidor con el estado ";
        }
        else if(estadoPedido==null && idUsuario!=null && idRepartidor==null){
            listaPedidos = (List<Pedido>) pedidoRepository.findAllByUsuario(usuario);
            if(listaPedidos.isEmpty()){
                return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El usuario todavia no tiene pedidos", null);
            }
            mensaje = "Se ha obtenido lista de Pedidos del usuario ";
        }
        else if(estadoPedido==null && idUsuario==null && idRepartidor !=null){
            listaPedidos = (List<Pedido>) pedidoRepository.findAllByRepartidor(repartidor);
            if(listaPedidos.isEmpty()){
                return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El repartidor todavia no tiene pedidos", null);
            }
            mensaje = "Se ha obtenido lista de Pedidos del repartidor ";
        }
        else{
            listaPedidos = (List<Pedido>) pedidoRepository.findAllByUsuarioAndRepartidorAndEstadoPedido(usuario, repartidor, estadoPedido);
            mensaje = "Se ha obtenido lista de Pedidos del repartidor "+
                        ", del usuario "+
                        ", y con el estado ";
        }
//        listaPedidos=pedidoRepository.findPedidosByFilters(usuario,repartidor,estadoPedido);
        return new GenericResponse<>(
                TIPO_DATA,
                RPTA_OK,
                "mensaje",
                crearListaPedidosDTO(listaPedidos)
        );
    }
    //2.- Obtener un pedido en específico(ADMIN)
    public GenericResponse<PedidoDTOAdmin>obtenerPedido(long idPedido){
        Optional<Pedido> optP = pedidoRepository.findById(idPedido);
        if(optP.isPresent()){
            return new GenericResponse<>(TIPO_DATA, RPTA_OK,"Se ha obtenido el pedido seleccionado",obtenerPedidoDto(optP.get()));
        }else{
            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING,"Ese pedido no existe",null);
        }
    }
    //3.- Cambiar de estado un Pedido("preparar", "enviar","cancelar","completar")
    public GenericResponse<PedidoDTOAdmin> cambiarEstado(Long idPedido, String accion, Long idRepartidor){
        Optional<Usuario> optRep = (idRepartidor!=null) ? usuarioRepository.findById(idRepartidor) : Optional.empty();
        Optional<Pedido>optP = pedidoRepository.findById(idPedido);
        if(!optRep.isPresent() && idRepartidor!=null){
            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se encuentra el repartidor introducido",null);
        }
        if (optP.isPresent()){
            Pedido p = optP.get();
            //ValidarPago
            if(accion.equalsIgnoreCase("preparar")){
                return prepararPedido(p);
            }
            if(accion.equalsIgnoreCase("enviar")){
                if(idRepartidor==null){
                    return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "Se necesita especificar un Repartidor para el envío", null);
                }
                Usuario repartidor = optRep.get();
                return enviarPedido(p, repartidor);
            }
            if(accion.equalsIgnoreCase("cancelar")){
                return cancelarPedido(p);
            }
            if(accion.equalsIgnoreCase("completar")){
                return entregarPedido(p);
            }
            else return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se ha introducido un comando valido",null);
        }else{
            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se encuentra el pedido", null);
        }
    }

    //Metodos para gestionar el cambio de estado de los pedidos
    public GenericResponse<PedidoDTOAdmin>prepararPedido(Pedido p) {
        if(p.getEstadoPedido()==PENDIENTE){
            p.setEstadoPedido(EN_PROCESO);
            return new GenericResponse<>(TIPO_DATA, RPTA_OK, "El pedido se está procesando", this.obtenerPedidoDto(p));
        }
        else if(p.getEstadoPedido()==CANCELADO){
            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El pedido no se puede procesar porque ha sido cancelado", this.obtenerPedidoDto(p));
        }else{
            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El pedido no se puede procesar porque ya ha sido procesado", this.obtenerPedidoDto(p));
        }
    }
    public GenericResponse<PedidoDTOAdmin>enviarPedido(Pedido p, Usuario repartidor) {
        try {
            if(p.getEstadoPedido()==EN_PROCESO && p.getRepartidor()==null){
                p.setRepartidor(repartidor);
                p.setEstadoPedido(ENVIADO);
                return new GenericResponse<>(TIPO_DATA, RPTA_OK, "El pedido se ha enviado", this.obtenerPedidoDto(p));
            }
            else if(p.getEstadoPedido()==CANCELADO){
                return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El pedido no se puede enviar porque ha sido cancelado", this.obtenerPedidoDto(p));
            }else if(p.getEstadoPedido()==PENDIENTE){
                return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El pedido no se puede enviar porque aún no se ha preparado", this.obtenerPedidoDto(p));
            } else{
                return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El pedido no se puede enviar porque ya ha sido enviado", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponse<>(TIPO_EX, RPTA_ERROR,e.getMessage(),null);
        }


    }
    public GenericResponse<PedidoDTOAdmin>cancelarPedido(Pedido p){
        if(p.getEstadoPedido()==ENTREGADO){
            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El pedido no se puede cancelar porque ya ha sido completado", null);
        }
        else if(p.getEstadoPedido()==CANCELADO){
            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El pedido no se puede cancelar porque ya ha sido cancelado", null);
        } else{
            p.setEstadoPedido(CANCELADO);
            p.setRepartidor(null);
            List<DetallePedido> listaDetalles = p.getDetalles();
            for(DetallePedido detalle : listaDetalles){
                Producto prod = detalle.getProducto();
                int cantidad = detalle.getCantidad();
                prod.setStock(prod.getStock()+cantidad);
            }
            return new GenericResponse<>(TIPO_DATA, RPTA_OK, "Pedido cancelado con éxito", this.obtenerPedidoDto(p));
        }
    }
    public GenericResponse<PedidoDTOAdmin>entregarPedido(Pedido p){
        if(p.getEstadoPedido()==CANCELADO){
            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se puede entregar el pedido porque se ha cancelado", null);
        }
        else if(p.getEstadoPedido() == ENVIADO){
            p.setEstadoPedido(ENTREGADO);
            return new GenericResponse<>(TIPO_DATA, RPTA_OK, "El pedido se ha entregado y completado con éxito", this.obtenerPedidoDto(p));
        }
        else {
            return new GenericResponse<>(TIPO_DATA, RPTA_OK, "El pedido todavía no se ha enviado", null);
        }
    }

    //Crear PedidoDTO y Lista PedidoDTO
    public PedidoDTOAdmin obtenerPedidoDto(Pedido p){
        List<DetalleDTO> listaDetalles = new ArrayList<>();
        for(DetallePedido detalle: p.getDetalles()){
            listaDetalles.add(new DetalleDTO(detalle));
        }
        return new PedidoDTOAdmin(p, listaDetalles);
    }
    public List<PedidoDTOAdmin>crearListaPedidosDTO(List<Pedido> listaPedidos){
        List<PedidoDTOAdmin>listaPedidosDTO = new ArrayList<>();
        for(Pedido pedido : listaPedidos){
            listaPedidosDTO.add(obtenerPedidoDto(pedido));
        }
        return listaPedidosDTO;
    }
//
//    //3.- ValidarPago (PENDIENTE --> EN PROCESO)
//    public GenericResponse<PedidoDTOAdmin>prepararPedido(long idPedido){
//        Optional<Pedido> optP = pedidoRepository.findById(idPedido);
//        if(optP.isPresent()){
//            Pedido pedido = optP.get();
//            if(pedido.getEstadoPedido()==PENDIENTE){
//                pedido.setEstadoPedido(EN_PROCESO);
//                return new GenericResponse<>(TIPO_DATA, RPTA_OK, "El pedido se está procesando", this.obtenerPedidoDto(pedido));
//            }
//            else{
//                return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El pedido no se puede procesar porque ya ha sido procesado", this.obtenerPedidoDto(pedido));
//            }
//        }else return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se encuentra el pedido", null);
//    }
    //4.- EnviarPedido(EN PROCESO --> ENVIADO)
//    public GenericResponse<PedidoDTOAdmin>asignarPedido(long idPedido, long idRepartidor){
//        Optional<Pedido> optP = pedidoRepository.findById(idPedido);
//        Optional<Usuario> optRepart = usuarioRepository.findById(idRepartidor);
//        if(!optP.isPresent()){
//            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se encuentra el pedido", null);
//        }
//        else if(!optRepart.isPresent()) {
//            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se encuentra el repartidor", null);
//        }
//        else if(optRepart.get().getRol()!= Rol.REPARTIDOR){
//            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El usuario introducido no es un repartidor", null);
//        }
//        else{
//            Pedido p = optP.get();
//            Usuario repartidor = optRepart.get();
//            if(p.getEstadoPedido()!=EN_PROCESO || p.getRepartidor()!=null){
//                return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El pedido no se puede enviar todavía o ya ha sido enviado", null);
//            }
//            else {
//                p.setRepartidor(repartidor);
//                p.setEstadoPedido(ENVIADO);
//                return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El pedido se ha enviado correctamente",
//                        this.obtenerPedidoDto(p));
//            }
//        }
//    }
    //5.- Cancelar Pedido(PENDIENTE, EN_PROCESO o ENVIADO --> CANCELADO
//    public GenericResponse<PedidoDTOAdmin> cancelarPedido(long idPedido){
//        Optional<Pedido>optP = pedidoRepository.findById(idPedido);
//        if(optP.isPresent()){
//            Pedido p = optP.get();
//            if(p.getEstadoPedido()==ENTREGADO){
//                return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El pedido no se puede cancelar porque ya ha sido completado", null);
//            }
//            else if(p.getEstadoPedido()==CANCELADO){
//                return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El pedido no se puede cancelar porque ya ha sido cancelado", null);
//            } else{
//                p.setEstadoPedido(CANCELADO);
//                p.setRepartidor(null);
//                List<DetallePedido> listaDetalles = p.getDetalles();
//                for(DetallePedido detalle : listaDetalles){
//                    Producto prod = detalle.getProducto();
//                    int cantidad = detalle.getCantidad();
//                    prod.setStock(prod.getStock()+cantidad);
//                }
//                return new GenericResponse<>(TIPO_DATA, RPTA_OK, "Pedido cancelado con éxito", this.obtenerPedidoDto(p));
//            }
//        }else{
//            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se encuentra el pedido a cancelar", null);
//        }
//    }
    //6.- Completar Pedido (ENVIADO --> ENTREGADO)
//    public GenericResponse<PedidoDTOAdmin> completarPedido(long idPedido){
//        Optional<Pedido>optP = pedidoRepository.findById(idPedido);
//        if(optP.isPresent()){
//            Pedido p = optP.get();
//            if(p.getEstadoPedido()==CANCELADO){
//                return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se puede entregar el pedido porque se ha cancelado", null);
//            }
//            else if(p.getEstadoPedido() == ENVIADO){
//                p.setEstadoPedido(ENTREGADO);
//                return new GenericResponse<>(TIPO_DATA, RPTA_OK, "El pedido se ha entregado y completado con éxito", this.obtenerPedidoDto(p));
//            }
//            else {
//                return new GenericResponse<>(TIPO_DATA, RPTA_OK, "El pedido todavía no se ha enviado", null);
//            }
//        }else{
//            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se ha encontrado el pedido", null);
//        }
//    }
}






































