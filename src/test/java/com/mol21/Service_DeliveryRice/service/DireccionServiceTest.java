package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.DTO.DireccionDTO;
import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.model.Usuario;
import com.mol21.Service_DeliveryRice.persistence.DireccionRepository;
import com.mol21.Service_DeliveryRice.persistence.UsuarioRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import net.bytebuddy.description.type.TypeList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DireccionServiceTest {

  @Mock
  private DireccionRepository direccionRepository;
  @InjectMocks
  private DireccionService direccionService;
  @Mock
  private UsuarioRepository usuarioRepository;

  private final String MESSAGE_LISTA_DIRECCIONES_OK="Se ha obtenido la lista de las direcciones del usuario ";
  private final String MESSAGE_USUARIO_NO_TIENE_DIRECCION_ACTIVA="El usuario introducido no tiene ninguna direccion activa";
  private final String MESSAGE_DIRECCION_PRINC_OK="Se ha devuelto la direccion principal del usuario";
  private final String MESSAGE_DIRECCION_PRINC_NOT_OK="No se ha encontrado direccion principal para ese usuario";
  private final String MESSAGE_DIRECCION_ERROR="La direccion introducida no es correcta";
  private final String MESSAGE_DIRECCION_YA_ES_PRINC="La direccion introducida ya es la principal";
  private final String MESSAGE_NEW_DIRECC_PRINC="dirección establecida como principal";
  private final String MESSAGE_USER_NOT_FOUND="No se encuentra el usuario";
  private final String MESSAGE_USER_YA_TIENE_DIRECCION="El usuario ya tiene esa direccion registrada";
  private final String MESSAGE_NEW_DIREC_OK="Se ha guardado la direccion con exito";
  private final String MESSAGE_DIREC_ACTUALIZADA_OK="Direccion actualizada correctamente";
  private final String MESSAGE_DIREC_ELIMINADA_OK="Direccion eliminada";
  private final String MESSAGE_DIREC_YA_ESTA_DESACTIVADA="La dirección ya está desactivada";
  private final String MESSAGE_DIREC_MIN_UNA_DIREC="Debe tener al menos una dirección";
  private final String MESSAGE_DIREC_DESACTIV_OK="Direccion desactivada";

  @Test
  public void obtener_direcciones_fail_Test(){

    long usuarioId = 31;
    List<Direccion>listaDirecciones = new ArrayList<>();

    when(direccionRepository.findByUsuario_idAndEsActivaTrue(usuarioId)).thenReturn(listaDirecciones);

    GenericResponse<List<DireccionDTO>>respuesta = direccionService.obtenerDirecciones(usuarioId);

    assertEquals(MESSAGE_USUARIO_NO_TIENE_DIRECCION_ACTIVA, respuesta.getMessage());
  }

  @Test
  public void obtener_direcciones_ok_Test(){

    long usuarioId = 31;
    List<Direccion>listaDirecciones = new ArrayList<>();
    Direccion d1 = new Direccion();
    Direccion d2 = new Direccion();
    d1.setDireccion_id(432L);
    d2.setDireccion_id(5423543L);
    listaDirecciones.add(d1);
    listaDirecciones.add(d2);

    when(direccionRepository.findByUsuario_idAndEsActivaTrue(usuarioId)).thenReturn(listaDirecciones);

    GenericResponse<List<DireccionDTO>>respuesta = direccionService.obtenerDirecciones(usuarioId);

    assertEquals(MESSAGE_LISTA_DIRECCIONES_OK, respuesta.getMessage());
  }

  @Test
  public void get_principal_fail_Test(){
    long idUsuario = 31;
    when(direccionRepository.findByUsuario_idAndEsPrincipalTrue(idUsuario)).thenReturn(Optional.empty());
    GenericResponse<DireccionDTO> respuesta = direccionService.getPrincipal(idUsuario);
    assertEquals(MESSAGE_DIRECCION_PRINC_NOT_OK,respuesta.getMessage());
  }

  @Test
  public void get_principal_ok_Test(){
    long idUsuario = 31;
    Direccion d = new Direccion();
    d.setDireccion_id(31L);
    when(direccionRepository.findByUsuario_idAndEsPrincipalTrue(idUsuario)).thenReturn(Optional.of(d));
    GenericResponse<DireccionDTO> respuesta = direccionService.getPrincipal(idUsuario);
    assertEquals(MESSAGE_DIRECCION_PRINC_OK,respuesta.getMessage());
  }
  @Test
  public void set_direccion_principal_fail_test(){
    Direccion d = crearDireccion();
    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.empty());
    GenericResponse<DireccionDTO>respuesta = direccionService.setDireccionPrincipal(d.getDireccion_id());
    assertEquals(MESSAGE_DIRECCION_ERROR,respuesta.getMessage());

  }
  @Test
  public void set_direccion_principal_fail_ya_princ_test(){
    Direccion d = crearDireccion();
    d.setEsPrincipal(true);
    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.of(d));
    GenericResponse<DireccionDTO>respuesta = direccionService.setDireccionPrincipal(d.getDireccion_id());
    assertEquals(MESSAGE_DIRECCION_YA_ES_PRINC,respuesta.getMessage());

  }
  @Test
  public void set_direccion_principal_ok_test(){
    Direccion d = crearDireccion();
    Usuario u = crearUsuario();
    d.setUsuario(u);
    d.setEsPrincipal(false);
    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.of(d));
    GenericResponse<DireccionDTO>respuesta = direccionService.setDireccionPrincipal(d.getDireccion_id());
    assertEquals(MESSAGE_NEW_DIRECC_PRINC,respuesta.getMessage());

  }




















  @Test
  public void guardar_direccion_user_not_found_Test(){
    Direccion d = new Direccion();
    long usuarioID = 21;
    when(usuarioRepository.findById(usuarioID)).thenReturn(Optional.empty());
    GenericResponse<DireccionDTO> respuesta = direccionService.guardarDireccion(usuarioID,d);
    assertEquals(MESSAGE_USER_NOT_FOUND,respuesta.getMessage());
  }

  @Test
  public void guardar_direccion_user_already_direccion_register(){
    Direccion d = crearDireccion();
    Usuario u = crearUsuario();

    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(direccionRepository.findByUsuarioAndCalleAndNumeroAndCiudadAndCodPostal(u,d.getCalle(),d.getNumero(),d.getCiudad(),d.getCodPostal()))
            .thenReturn(Optional.of(d));

    GenericResponse<DireccionDTO>respuesta = direccionService.guardarDireccion(u.get_id(),d);

    assertEquals(MESSAGE_USER_YA_TIENE_DIRECCION,respuesta.getMessage());

  }
  @Test
  public void guardar_direccion_ok_Test(){

    Direccion d = crearDireccion();
    Usuario u = crearUsuario();

    when(usuarioRepository.findById(u.get_id())).thenReturn(Optional.of(u));
    when(direccionRepository.findByUsuarioAndCalleAndNumeroAndCiudadAndCodPostal(u,d.getCalle(),d.getNumero(),d.getCiudad(),d.getCodPostal()))
            .thenReturn(Optional.empty());


    GenericResponse<DireccionDTO> respuesta = direccionService.guardarDireccion(u.get_id(),d);

    assertEquals(MESSAGE_NEW_DIREC_OK,respuesta.getMessage());

  }

  @Test
  public void modificar_direccion_ok_test(){
    Direccion d = crearDireccion();
    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.of(d));
    GenericResponse<DireccionDTO>respuesta = direccionService.modificarDireccion(d);
    assertEquals(MESSAGE_DIREC_ACTUALIZADA_OK,respuesta.getMessage());
  }

  @Test
  public void modificar_direccion_fail_test(){
    Direccion d = crearDireccion();
    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.empty());
    GenericResponse<DireccionDTO>respuesta = direccionService.modificarDireccion(d);
    assertEquals(MESSAGE_DIRECCION_ERROR,respuesta.getMessage());
  }

  @Test
  public void eliminar_direccion_ok(){
    Direccion d = crearDireccion();
    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.of(d));
    GenericResponse<DireccionDTO>respuesta = direccionService.eliminarDireccion(d.getDireccion_id());
    assertEquals(MESSAGE_DIREC_ELIMINADA_OK,respuesta.getMessage());
  }

  @Test
  public void eliminar_direccion_fail(){
    Direccion d = crearDireccion();
    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.empty());
    GenericResponse<DireccionDTO>respuesta = direccionService.eliminarDireccion(d.getDireccion_id());
    assertEquals(MESSAGE_DIRECCION_ERROR,respuesta.getMessage());
  }

  @Test
  public void desactivar_direccion_ok(){
    Direccion d = crearDireccion();
    Direccion d2 = crearDireccion();
    d.setEsActiva(true);
    Usuario u = crearUsuario();
    d.setUsuario(u);
    List<Direccion>listaDirecciones = new ArrayList();
    listaDirecciones.add(d);
    listaDirecciones.add(d2);

    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.of(d));
    when(direccionRepository.findByUsuario_idAndEsActivaTrue(d.getUsuario().get_id()))
            .thenReturn(listaDirecciones);
    GenericResponse<DireccionDTO>respuesta = direccionService.desactivarDireccion(d.getDireccion_id());

    assertEquals(MESSAGE_DIREC_DESACTIV_OK,respuesta.getMessage());

  }
  @Test
  public void desactivar_direccion_fail_test(){
    Direccion d = crearDireccion();

    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.empty());
    GenericResponse<DireccionDTO>respuesta = direccionService.desactivarDireccion(d.getDireccion_id());
    assertEquals(MESSAGE_DIRECCION_ERROR,respuesta.getMessage());
  }
  @Test
  public void desactivar_direccion_fail_almenos_1_direccion(){
    Direccion d = crearDireccion();
    d.setEsActiva(true);
    Usuario u = crearUsuario();
    d.setUsuario(u);
    List<Direccion>listaDirecciones = new ArrayList();
    listaDirecciones.add(d);

    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.of(d));
    when(direccionRepository.findByUsuario_idAndEsActivaTrue(d.getUsuario().get_id()))
            .thenReturn(listaDirecciones);
    GenericResponse<DireccionDTO>respuesta = direccionService.desactivarDireccion(d.getDireccion_id());

    assertEquals(MESSAGE_DIREC_MIN_UNA_DIREC,respuesta.getMessage());

  }
  @Test
  public void desactivar_direccion_fail_direc_ya_desactivada(){
    Direccion d = crearDireccion();
    Usuario u = crearUsuario();
    d.setUsuario(u);
    d.setEsActiva(false);
    List<Direccion>listaDirecciones = new ArrayList();
    listaDirecciones.add(d);

    when(direccionRepository.findById(d.getDireccion_id())).thenReturn(Optional.of(d));
    when(direccionRepository.findByUsuario_idAndEsActivaTrue(d.getUsuario().get_id()))
            .thenReturn(listaDirecciones);
    GenericResponse<DireccionDTO>respuesta = direccionService.desactivarDireccion(d.getDireccion_id());
    System.out.println("El tamaño de la lista es: "+listaDirecciones.size());
    System.out.println("El mensaje de la respuesta es: "+respuesta.getMessage());
    assertEquals(MESSAGE_DIREC_YA_ESTA_DESACTIVADA,respuesta.getMessage());
  }

  private Usuario crearUsuario() {
    long usuarioID = 21;
    Usuario u = new Usuario();
    u.set_id(usuarioID);
    return u;
  }


  private Direccion crearDireccion() {
    Direccion d = new Direccion();
    d.setDireccion_id(31L);
    d.setCalle("Rio de Janeiro");
    d.setNumero("31");
    d.setCiudad("Barcelona");
    d.setCodPostal("123123");
    return d;
  }


}
