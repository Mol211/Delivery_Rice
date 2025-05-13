package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.DTO.PedidoDTO;
import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.model.EstadoPedido;
import com.mol21.Service_DeliveryRice.model.Pedido;
import com.mol21.Service_DeliveryRice.model.Usuario;
import com.mol21.Service_DeliveryRice.persistence.PedidoRepository;
import com.mol21.Service_DeliveryRice.persistence.UsuarioRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mol21.Service_DeliveryRice.model.EstadoPedido.ENVIADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {
  @Mock
  private PedidoRepository pedidoRepository;
  @Mock
  private UsuarioRepository usuarioRepository;
  @InjectMocks
  private CheckoutService checkoutService;

  private final String MESSAGE_OK_OBTENER_PEDIDOS = "Se ha obtenido lista de Pedidos del repartidor "+
          ", del usuario "+
          ", y con el estado ";
  private final String MESSAGE_OK_OBTENER_PEDIDO = "Se ha obtenido el pedido seleccionado";
  private final String MESSAGE_OK_PREPARAR_PEDIDO = "El pedido se está procesando";
  private final String MESSAGE_OK_ENVIAR_PEDIDO = "El pedido se ha enviado";
  private final String MESSAGE_OK_CANCELAR_PEDIDO = "Pedido cancelado con éxito";
  private final String MESSAGE_OK_ENTREGAR_PEDIDO = "El pedido se ha entregado y completado con éxito";
  private final String MESSAGE_FAIL_NO_USUARIO = "No se ha encontrado el usuario";
  private final String MESSAGE_FAIL_NO_REPARTIDOR = "No se ha encontrado el repartidor";
  private final String MESSAGE_FAIL_NO_PEDIDO = "No se encuentra el pedido";
  private final String MESSAGE_FAIL_NEED_REPARTIDOR = "Se necesita especificar un Repartidor para el envío";
  private final String MESSAGE_FAIL_COMANDO_NOT_VALID = "No se ha introducido un comando valido";

  @Test
  public void obtener_pedidos_ok(){
    Usuario cliente = new Usuario();
    Usuario repartidor = new Usuario();
    cliente.set_id(1L);
    repartidor.set_id(2L);
    when(usuarioRepository.findById(cliente.get_id())).thenReturn(Optional.of(cliente));
    when(usuarioRepository.findById(repartidor.get_id())).thenReturn(Optional.of(repartidor));
    GenericResponse<List<PedidoDTO>>response = checkoutService.obtenerPedidos(cliente.get_id(), repartidor.get_id(), EstadoPedido.ENVIADO);
    assertEquals(MESSAGE_OK_OBTENER_PEDIDOS,response.getMessage());
  }
  @Test
  public void obtener_pedidos_fail_no_usuario(){
    Usuario cliente = new Usuario();
    Usuario repartidor = new Usuario();
    when(usuarioRepository.findById(cliente.get_id())).thenReturn(Optional.empty());
    GenericResponse<List<PedidoDTO>>response = checkoutService.obtenerPedidos(cliente.get_id(), repartidor.get_id(), EstadoPedido.ENVIADO );
    assertEquals(MESSAGE_FAIL_NO_USUARIO,response.getMessage());
  }
  @Test
  public void obtener_pedidos_fail_no_repartidor(){
    Usuario cliente = new Usuario();
    Usuario repartidor = new Usuario();
    cliente.set_id(1L);
    repartidor.set_id(2L);
    when(usuarioRepository.findById(cliente.get_id())).thenReturn(Optional.of(cliente));
    when(usuarioRepository.findById(repartidor.get_id())).thenReturn(Optional.empty());
    GenericResponse<List<PedidoDTO>>response = checkoutService.obtenerPedidos(cliente.get_id(), repartidor.get_id(), EstadoPedido.ENVIADO );
    assertEquals(MESSAGE_FAIL_NO_REPARTIDOR,response.getMessage());
  }
  @Test
  public void obtener_pedido_fail_no_pedido(){
    Pedido p = new Pedido();
    when(pedidoRepository.findById(p.getId_pedido())).thenReturn(Optional.empty());
    GenericResponse<PedidoDTO> response = checkoutService.obtenerPedido(p.getId_pedido());
    assertEquals(MESSAGE_FAIL_NO_PEDIDO, response.getMessage());
  }
  @Test
  public void obtener_pedido_ok(){
    Pedido p = new Pedido();
    Direccion d = new Direccion();
    d.setId(12L);
    p.setDireccionEnvio(d);
    when(pedidoRepository.findById(p.getId_pedido())).thenReturn(Optional.of(p));
    GenericResponse<PedidoDTO> response = checkoutService.obtenerPedido(p.getId_pedido());
    assertEquals(MESSAGE_OK_OBTENER_PEDIDO, response.getMessage());
  }
  @Test
  public void cambiar_estado_fail_no_repartidor(){
    Usuario u = new Usuario();
    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.empty());
    GenericResponse<PedidoDTO>response = checkoutService.cambiarEstado(12L,"",u.get_id());
    assertEquals(MESSAGE_FAIL_NO_REPARTIDOR,response.getMessage());
  }
  @Test
  public void cambiar_estado_fail_no_comand(){
    Usuario u = new Usuario();
    Pedido p = new Pedido();
    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(pedidoRepository.findById(p.getId_pedido())).thenReturn(Optional.of(p));
    GenericResponse<PedidoDTO>response = checkoutService.cambiarEstado(p.getId_pedido(),"",u.get_id());
    assertEquals(MESSAGE_FAIL_COMANDO_NOT_VALID,response.getMessage());
  }
  @Test
  public void entregar_pedido_ok(){
    Usuario u = new Usuario();
    Pedido p = new Pedido();
    p.setEstadoPedido(EstadoPedido.ENVIADO);
    Direccion d = new Direccion();
    d.setId(12L);
    p.setDireccionEnvio(d);
    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(pedidoRepository.findById(p.getId_pedido())).thenReturn(Optional.of(p));
    GenericResponse<PedidoDTO>response = checkoutService.cambiarEstado(p.getId_pedido(),"completar",u.get_id());
    assertEquals(MESSAGE_OK_ENTREGAR_PEDIDO,response.getMessage());
  }
  @Test
  public void cancelar_pedido_ok(){
    Usuario u = new Usuario();
    Pedido p = new Pedido();
    p.setEstadoPedido(EstadoPedido.EN_PROCESO);
    Direccion d = new Direccion();
    d.setId(12L);
    p.setDireccionEnvio(d);
    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(pedidoRepository.findById(p.getId_pedido())).thenReturn(Optional.of(p));
    GenericResponse<PedidoDTO>response = checkoutService.cambiarEstado(p.getId_pedido(),"cancelar",u.get_id());
    assertEquals(MESSAGE_OK_CANCELAR_PEDIDO,response.getMessage());
  }
  @Test
  public void preparar_pedido_ok(){
    Usuario u = new Usuario();
    Pedido p = new Pedido();
    p.setEstadoPedido(EstadoPedido.PENDIENTE);
    Direccion d = new Direccion();
    d.setId(12L);
    p.setDireccionEnvio(d);
    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(pedidoRepository.findById(p.getId_pedido())).thenReturn(Optional.of(p));
    GenericResponse<PedidoDTO>response = checkoutService.cambiarEstado(p.getId_pedido(),"preparar",u.get_id());
    assertEquals(MESSAGE_OK_PREPARAR_PEDIDO,response.getMessage());



  }
  @Test
  public void enviar_pedido_ok(){
    Usuario u = new Usuario();
    Pedido p = new Pedido();
    p.setEstadoPedido(EstadoPedido.EN_PROCESO);
    Direccion d = new Direccion();
    d.setId(12L);
    p.setDireccionEnvio(d);
    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(pedidoRepository.findById(p.getId_pedido())).thenReturn(Optional.of(p));
    GenericResponse<PedidoDTO>response = checkoutService.cambiarEstado(p.getId_pedido(),"enviar",u.get_id());
    assertEquals(MESSAGE_OK_ENVIAR_PEDIDO,response.getMessage());
  }


}
