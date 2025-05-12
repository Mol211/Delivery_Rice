package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.*;
import com.mol21.Service_DeliveryRice.model.DTO.CarritoDTO;
import com.mol21.Service_DeliveryRice.model.DTO.ItemDTO;
import com.mol21.Service_DeliveryRice.persistence.*;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mol21.Service_DeliveryRice.utils.Global.RPTA_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class CarritoServiceTest {

  @Mock
  CarritoRepository carritoRepository;
  @Mock
  UsuarioRepository usuarioRepository;
  @Mock
  DireccionRepository direccionRepository;
  @InjectMocks
  CarritoService carritoService;

  private final String MESSAGE_OK_NEW_CARRITO="Se ha creado un nuevo carrito";
  private final String MESSAGE_OK_VACIAR_CARRITO="Se ha vaciado el carrito";
  private final String MESSAGE_OK_GET_CARRITO_SIN_PROC="Se ha obtenido el carrito sin procesar";
  private final String MESSAGE_OK_CARRITO_PUEDE_PROCES="El carrito puede procesarse, no sobrepasa el stock de los productos";
  private final String MESSAGE_OK_PEDIDO_PROCESADO="Se ha procedo el carrito y se ha creado el pedido";
  private final String MESSAGE_FAIL_CARRITO_ACTIVO="No se puede crear un nuevo carrito mientras tengas uno activo";
  private final String MESSAGE_FAIL_USUARIO_NOT_FOUND="No se ha encontrado el usuario";
  private final String MESSAGE_FAIL_CARRITO_YA_VACIO="El carrito ya está vacío";
  private final String MESSAGE_FAIL_CARRITO_NO_EXISTE="No existe ese Carrito";
  private final String MESSAGE_FAIL_CARRITO_NO_ACTIVO="No tiene carrito activo";
  private final String MESSAGE_FAIL_CARRITO_NO_PROCESA_ESTA_VACIO="El carrito no puede procesarse porque está vacío";
  private final String MESSAGE_FAIL_DIRECC_NOT_FOUND="No se ha encontrado la dirección";
  private final String MESSAGE_FAIL_PRODUCTOS_EXCESO_STOCK="El carrito no puede procesarse porque uno o más elementos superan el stock permitido" +
          ", retírelos del carrito para continuar";

  @Test
  public void nuevo_carrito_fail_user_not_found_test(){
    Usuario u = new Usuario();
    Carrito c = new Carrito();
    u.set_id(31);

    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.empty());
    GenericResponse<CarritoDTO> respuesta = carritoService.nuevoCarrito(u.get_id());
    assertEquals(MESSAGE_FAIL_USUARIO_NOT_FOUND,respuesta.getMessage());
  }
  @Test
  public void nuevo_carrito_fail_carrito_activo_test(){
    Usuario u = new Usuario();
    Carrito c = new Carrito();
    u.set_id(31);

    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(carritoRepository.findByUsuarioAndProcesadoFalse(u)).thenReturn(Optional.of(c));
    GenericResponse<CarritoDTO> respuesta = carritoService.nuevoCarrito(u.get_id());
    assertEquals(MESSAGE_FAIL_CARRITO_ACTIVO,respuesta.getMessage());
  }
  @Test
  public void nuevo_carrito_ok_test(){
    Usuario u = new Usuario();
    Carrito c = new Carrito();
    u.set_id(31);

    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(carritoRepository.findByUsuarioAndProcesadoFalse(u)).thenReturn(Optional.empty());
    GenericResponse<CarritoDTO> respuesta = carritoService.nuevoCarrito(u.get_id());
    assertEquals(MESSAGE_OK_NEW_CARRITO,respuesta.getMessage());
  }
  @Test
  public void vaciar_carrito_ok_test(){
    Usuario u = new Usuario();
    Carrito c = new Carrito();
    ItemCarrito i1 = new ItemCarrito();
    List<ItemCarrito>listaItems = new ArrayList<>();
    listaItems.add(i1);
    c.setItemCarritoList(listaItems);
    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(carritoRepository.findByUsuarioAndProcesadoFalse(u)).thenReturn(Optional.of(c));
    GenericResponse<CarritoDTO>respuesta = carritoService.vaciarCarrito(u.get_id());
    assertEquals(MESSAGE_OK_VACIAR_CARRITO,respuesta.getMessage());
  }
  @Test
  public void vaciar_carrito_fail_carrito_ya_vacio_test(){
    Usuario u = new Usuario();
    Carrito c = new Carrito();
    List<ItemCarrito>listaItems = new ArrayList<>();
    c.setItemCarritoList(listaItems);
    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(carritoRepository.findByUsuarioAndProcesadoFalse(u)).thenReturn(Optional.of(c));
    GenericResponse<CarritoDTO>respuesta = carritoService.vaciarCarrito(u.get_id());
    assertEquals(MESSAGE_FAIL_CARRITO_YA_VACIO,respuesta.getMessage());

  }
  @Test
  public void vaciar_carrito_fail_carrito_not_found_test(){
    Usuario u = new Usuario();
    Carrito c = new Carrito();
    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(carritoRepository.findByUsuarioAndProcesadoFalse(u)).thenReturn(Optional.empty());
    GenericResponse<CarritoDTO>respuesta = carritoService.vaciarCarrito(u.get_id());
    assertEquals(MESSAGE_FAIL_CARRITO_NO_EXISTE,respuesta.getMessage());

  }
  @Test
  public void vaciar_carrito_user_not_found_test(){
    Usuario u = new Usuario();
    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.empty());
    GenericResponse<CarritoDTO>respuesta = carritoService.vaciarCarrito(u.get_id());
    assertEquals(MESSAGE_FAIL_USUARIO_NOT_FOUND,respuesta.getMessage());

  }

  @Test
  public void obtener_carrito_ok_test(){
    Usuario u = new Usuario();
    Carrito c = new Carrito();

    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(carritoRepository.findByUsuarioAndProcesadoFalse(u)).thenReturn(Optional.of(c));


    GenericResponse<CarritoDTO>respuesta = carritoService.obtenerCarrito(u.get_id());
    assertEquals(MESSAGE_OK_GET_CARRITO_SIN_PROC,respuesta.getMessage());


  }

  @Test
  public void obtener_carrito_fail_no_activo_test(){
    Usuario u = new Usuario();
    Carrito c = new Carrito();

    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(carritoRepository.findByUsuarioAndProcesadoFalse(u)).thenReturn(Optional.empty());


    GenericResponse<CarritoDTO>respuesta = carritoService.obtenerCarrito(u.get_id());
    assertEquals(MESSAGE_FAIL_CARRITO_NO_ACTIVO,respuesta.getMessage());

  }

  @Test
  public void obtener_carrito_fail_no_user_test(){
    Usuario u = new Usuario();

    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.empty());

    GenericResponse<CarritoDTO>respuesta = carritoService.obtenerCarrito(u.get_id());
    assertEquals(MESSAGE_FAIL_USUARIO_NOT_FOUND,respuesta.getMessage());

  }

  @Test
  public void validar_carrito_ok_test() {
    Carrito c = new Carrito();
    List<ItemCarrito>listaItems = new ArrayList<>();
    ItemCarrito i1 = new ItemCarrito();
    Producto p = new Producto();
    p.setStock(20);
    p.setPrecio(BigDecimal.TEN);
    i1.setProducto(p);
    i1.setCantidad(4);
    listaItems.add(i1);
    c.setItemCarritoList(listaItems);

    when(carritoRepository.findById(c.getCarrito_id())).thenReturn(Optional.of(c));

    GenericResponse<Map<ItemDTO, Integer>>respuesta = carritoService.validarCarrito(c.getCarrito_id());

    assertEquals(MESSAGE_OK_CARRITO_PUEDE_PROCES, respuesta.getMessage());
  }
  @Test
  public void validar_carrito_fail_stock_test(){
    Carrito c = new Carrito();
    List<ItemCarrito>listaItems = new ArrayList<>();
    ItemCarrito i1 = new ItemCarrito();
    Producto p = new Producto();
    p.setStock(2);
    p.setPrecio(BigDecimal.TEN);
    i1.setProducto(p);
    i1.setCantidad(4);
    listaItems.add(i1);
    c.setItemCarritoList(listaItems);

    when(carritoRepository.findById(c.getCarrito_id())).thenReturn(Optional.of(c));

    GenericResponse<Map<ItemDTO, Integer>>respuesta = carritoService.validarCarrito(c.getCarrito_id());

    assertEquals(MESSAGE_FAIL_PRODUCTOS_EXCESO_STOCK, respuesta.getMessage());

  }

  @Test
  public void validar_carrito_fail_esta_vacio_test(){
    Carrito c = new Carrito();
    List<ItemCarrito>listaItems = new ArrayList<>();
    c.setItemCarritoList(listaItems);

    when(carritoRepository.findById(c.getCarrito_id())).thenReturn(Optional.of(c));


    GenericResponse<Map<ItemDTO, Integer>>respuesta = carritoService.validarCarrito(c.getCarrito_id());

    assertEquals(MESSAGE_FAIL_CARRITO_NO_PROCESA_ESTA_VACIO, respuesta.getMessage());
  }

  @Test
  public void validar_carrito_fail_carrito_not_found_test(){
      Carrito c = new Carrito();

      when(carritoRepository.findById(c.getCarrito_id())).thenReturn(Optional.empty());

      GenericResponse<Map<ItemDTO, Integer>>respuesta = carritoService.validarCarrito(c.getCarrito_id());

      assertEquals(MESSAGE_FAIL_CARRITO_NO_EXISTE, respuesta.getMessage());



  }

  @Test
  public void procesar_pedido_fail_direccion_not_found_test(){
    Direccion d = new Direccion();
    d.setId(12L);
    Carrito c = new Carrito();

    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.empty());
    GenericResponse<Object>response = carritoService.procesarPedido(c.getCarrito_id(), MetodoPago.TARJETA, d.getDireccion_id());

    assertEquals(MESSAGE_FAIL_DIRECC_NOT_FOUND,response.getMessage());

  }
  @Test
  public void procesar_pedido_fail_carrito_not_found_test(){
    Direccion d = new Direccion();
    d.setId(12L);
    Carrito c = new Carrito();

    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.of(d));
    when(carritoRepository.findById(c.getCarrito_id())).thenReturn(Optional.empty());
    GenericResponse<Object>response = carritoService.procesarPedido(c.getCarrito_id(), MetodoPago.TARJETA, d.getDireccion_id());

    assertEquals(MESSAGE_FAIL_CARRITO_NO_EXISTE,response.getMessage());

  }
  @Test
  public void procesar_pedido_ok_test(){
    Direccion d = new Direccion();
    d.setId(12L);
    Usuario u = new Usuario();
    Carrito c = new Carrito();
    c.setUsuario(u);
    ItemCarrito i = new ItemCarrito();
    Producto p = new Producto();
    p.setPrecio(BigDecimal.TWO);
    p.setStock(10);
    i.setItem_id(123);
    i.setProducto(p);
    i.setCantidad(1);

    i.setSubTotal(BigDecimal.TEN);
    List<ItemCarrito>listaItems = new ArrayList<>();
    listaItems.add(i);
    c.setItemCarritoList(listaItems);



    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.of(d));
    when(carritoRepository.findById(c.getCarrito_id())).thenReturn(Optional.of(c));
    GenericResponse<Object>response = carritoService.procesarPedido(c.getCarrito_id(), MetodoPago.TARJETA, d.getDireccion_id());

    assertEquals(MESSAGE_OK_PEDIDO_PROCESADO,response.getMessage());

  }







}
