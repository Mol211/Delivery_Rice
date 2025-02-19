package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.DTO.DetalleDTO;
import com.mol21.Service_DeliveryRice.model.DTO.PedidoDTOAdmin;
import com.mol21.Service_DeliveryRice.model.DetallePedido;
import com.mol21.Service_DeliveryRice.model.Pedido;
import com.mol21.Service_DeliveryRice.model.Usuario;
import com.mol21.Service_DeliveryRice.persistence.PedidoRepository;
import com.mol21.Service_DeliveryRice.persistence.UsuarioRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mol21.Service_DeliveryRice.utils.Global.*;

@Service
@Transactional
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;


    public PedidoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }



    /*Métodos del Administrador*/
    //1.- Obtener Todos los Pedidos
    //2.- Obtener un pedido en específico (idPedido)
    //3.- Asignar un repartidor al Pedido(idPedido/asignar-repartidor/idRepartidor)
    //4.- Obtener Pedidos No completados
    //5.- Obtener Pedidos completados
    //6.- Obtener Historial de un repartidor
    //7.- Obtener Historial de usuario
    //8.- ValidarPago (prepara el pedido)
    //9.- EnviarPedido(asigna repartidor)
    //10.- Cancelar un pedido

    /*Metodo del Repartidor*/
    //11.-  CompletarPedido( entrega pedido y repartidor queda libre)

    /*Métodos de Usuario*/
    //12.-Obtener Todos los pedidos de ese Usuario

    //1.-Obtener todos los pedidos (ADMIN)
    public GenericResponse<List<PedidoDTOAdmin>>obtenerPedidos() {
        List<Pedido>listaPedidos = (List<Pedido>) pedidoRepository.findAll();
        return new GenericResponse<>(
                TIPO_DATA,
                RPTA_OK,
                "Se ha obtenido lista de Pedidos",
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
    //3.- Asignar un repartidor al Pedido(ADMIN)

    //4.- Obtener Pedidos en Curso (ADMIN)

    //5.- Obtener Pedidos Completados(ADMIN)

    //6.- Obtener Historial de un Repartidor(ADMIN)

    //7.- Obtener Historial de un Usuario (ADMIN)
    public GenericResponse<List<PedidoDTOAdmin>>obtenerPedidosUsuario(long idUsuario){
        Optional<Usuario> optU = usuarioRepository.findById(idUsuario);
        if(optU.isPresent()){
            Usuario usuario = optU.get();
            List<Pedido>listaPedidos =pedidoRepository.findAllByUsuario(usuario);
            if(listaPedidos.isEmpty()){
                return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "El usuario todavia no tiene pedidos", null);
            }else{
                return new GenericResponse<>(TIPO_DATA,RPTA_OK,
                        "Se ha obtenido lista de Pedidos",
                        crearListaPedidosDTO(listaPedidos)
                );
            }
        }else{
            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se encuentra el usuario", null);
        }
    }
    //8.- Actualizar Estado Pedido(ADMIN, REPARTIDOR)


    // 9.-Obtener Todos los pedidos de ese Usuario

    //


    //Crear PedidoDTO
    public PedidoDTOAdmin obtenerPedidoDto(Pedido p){
        List<DetalleDTO> listaDetalles = new ArrayList<>();
        for(DetallePedido detalle: p.getDetalles()){
            listaDetalles.add(new DetalleDTO(detalle));
        }
        return new PedidoDTOAdmin(p, listaDetalles);
    }
    //Crear Lista PedidosDTO
    public List<PedidoDTOAdmin>crearListaPedidosDTO(List<Pedido> listaPedidos){
        List<PedidoDTOAdmin>listaPedidosDTO = new ArrayList<>();
        for(Pedido pedido : listaPedidos){
            listaPedidosDTO.add(obtenerPedidoDto(pedido));
        }
        return listaPedidosDTO;
    }
}
