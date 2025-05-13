package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.DTO.PedidoDTO;
import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.model.Pedido;
import com.mol21.Service_DeliveryRice.model.Usuario;
import com.mol21.Service_DeliveryRice.persistence.PedidoRepository;
import com.mol21.Service_DeliveryRice.persistence.UsuarioRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {
  @Mock
  private PedidoRepository pedidoRepository;
  @Mock
  private UsuarioRepository usuarioRepository;
  @InjectMocks
  private PedidoService pedidoService;

  private final String MESSAGE_OK_OBTENER_PEDIDOS = "Se ha obtenido la lista de pedidos";
  private final String MESSAGE_OK_OBTENER_PEDIDO = "Se ha obtenido el pedido seleccionado";
  private final String MESSAGE_FAIL_NO_USUARIO = "No se encuentra ese usuario";
  private final String MESSAGE_FAIL_NO_PEDIDO = "No se encuentra el pedido";

  @Test
  public void obtener_pedidos_ok(){
    Usuario u = new Usuario();
    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(pedidoRepository.findAllByUsuario(u)).thenReturn(new ArrayList<>());
    GenericResponse<List<PedidoDTO>>response = pedidoService.obtenerPedidosUsuario(u.get_id());
    assertEquals(MESSAGE_OK_OBTENER_PEDIDOS,response.getMessage());
  }
  @Test
  public void obtener_pedidos_fail(){
    Usuario u = new Usuario();
    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.empty( ));
    GenericResponse<List<PedidoDTO>>response = pedidoService.obtenerPedidosUsuario(u.get_id());
    assertEquals(MESSAGE_FAIL_NO_USUARIO,response.getMessage());
  }
  @Test
  public void obtener_pedido_fail(){
    Pedido p = new Pedido();
    when(pedidoRepository.findById(p.getId_pedido())).thenReturn(Optional.empty());
    GenericResponse<PedidoDTO> response = pedidoService.obtenerPedidoId(p.getId_pedido());
    assertEquals(MESSAGE_FAIL_NO_PEDIDO,response.getMessage());
  }
  @Test
  public void obtener_pedido_ok(){
    Pedido p = new Pedido();
    Direccion d = new Direccion();
    d.setId(123L);
    p.setDireccionEnvio(d);
    when(pedidoRepository.findById(p.getId_pedido())).thenReturn(Optional.of(p));
    GenericResponse<PedidoDTO> response = pedidoService.obtenerPedidoId(p.getId_pedido());
    assertEquals(MESSAGE_OK_OBTENER_PEDIDO,response.getMessage());
  }
}
