package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.DTO.DetalleDTO;
import com.mol21.Service_DeliveryRice.model.DTO.PedidoDTO;
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

    //Constructor
    public PedidoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }
    //1.- Obtener Pedidos de Usuario
    public GenericResponse<List<PedidoDTO>> obtenerPedidosUsuario(long usuarioId) {
        Optional<Usuario> optU = usuarioRepository.findById(usuarioId);
        if (optU.isPresent()) {
            Usuario usuario=optU.get();
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_OK,
                    "Se ha obtenido la lista de pedidos",
                    crearListaPedidosDTO(pedidoRepository.findAllByUsuario(usuario))
            );
        } else {
            return new GenericResponse<>(TIPO_DATA, RPTA_WARNING, "No se encuentra ese usuario", new ArrayList<>());
        }
    }
    //2.- Obtener Pedido por Id
    public GenericResponse<PedidoDTO> obtenerPedidoId(long pedidoId){
        Optional<Pedido> optP = pedidoRepository.findById(pedidoId);
        if (optP.isPresent()) {
            Pedido pedido = optP.get();
            PedidoDTO pDTO = obtenerPedidoDto(pedido);
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_OK,
                    "Se ha obtenido el pedido seleccionado",
                    pDTO
            );
    } else {
            return new GenericResponse<>(TIPO_DATA,RPTA_WARNING,"No se encuentra el pedido",null);
        }
    }
    // Podría crear un metodo para repetir un pedido pasado, volver a meterlo en un carrito y crear un nuevo pedido. De momento no.
    //Crear PedidoDTO
    public PedidoDTO obtenerPedidoDto(Pedido p) {
        List<DetalleDTO> listaDetalles = new ArrayList<>();
        for (DetallePedido detalle : p.getDetalles()) {
            listaDetalles.add(new DetalleDTO(detalle));
        }
        return new PedidoDTO(p, listaDetalles);
    }

    //Crear Lista PedidosDTO
    public List<PedidoDTO> crearListaPedidosDTO(List<Pedido> listaPedidos) {
        List<PedidoDTO> listaPedidosDTO = new ArrayList<>();
        for (Pedido pedido : listaPedidos) {
            listaPedidosDTO.add(obtenerPedidoDto(pedido));
        }
        return listaPedidosDTO;
    }
}
