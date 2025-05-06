package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.exception.GenericExceptionHandler;
import com.mol21.Service_DeliveryRice.model.DTO.RegistrarUsuarioDTO;
import com.mol21.Service_DeliveryRice.model.DTO.UsuarioDTO;
import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.model.Rol;
import com.mol21.Service_DeliveryRice.model.Usuario;
import com.mol21.Service_DeliveryRice.persistence.DireccionRepository;
import com.mol21.Service_DeliveryRice.persistence.UsuarioRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import com.mol21.Service_DeliveryRice.utils.Global;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
  final String REGISTRAR_USUARIO_OK = "Se ha creado un nuevo usuario";
  final String EMAIL_YA_EXISTE = "Ya existe un usuario con ese correo, introduzca uno válido";
  final String LOGIN_OK = "Has iniciado sesión correctamente";
  final String LOGIN_PASS_NOT_OK = "Contraseña incorrecta";
  final String USUARIO_NO_ENCONTRADO = "Usuario no encontrado";
  final String MODIFICAR_CLIENTE_OK = "Se han actualizado los datos del usuario ";
  @Mock
  private UsuarioRepository usuarioRepository;
  @Mock
  private DireccionRepository direccionRepostitory;
  @Mock
  private PasswordEncoder passwordEncoder;
  @InjectMocks
  private UsuarioService usuarioService;
  //Tests a realizar:
  //1. registrar usuario con éxito
  //2. registrar usuario con email ya existente
  //4. iniciar sesión con éxito
  //5. iniciar sesión con una contraseña erronea
  //6. iniciar sesión con un usuario que no existe
  //7. modificar los datos de un usuario
  //8. modificar los datos de un usuario con un email ya existente
  //9. modificar los datos de un usuario con datos no válidos

  //1. registrar un usuario con éxito
  @Test
  public void registrar_usuario_Test(){
    //Creamos el usuario y la dirección
    Usuario u = new Usuario();
    u.setEmail("test@mail.com");
    u.setPassword("password");
    Direccion d = new Direccion();
    RegistrarUsuarioDTO regusuario = new RegistrarUsuarioDTO(u,d);

    //Hacemos el "mocking"
    when(usuarioRepository.existsByEmail("test@mail.com")).thenReturn(false);
//    when(passwordEncoder.encode("password")).thenReturn("PasswordEncoded");
//    when(usuarioRepository.save(any(Usuario.class))).thenReturn(u);
//    when(direccionRepostitory.save(any(Direccion.class))).thenReturn(d);

    //Ejecutamos el metodo a probar
    GenericResponse<UsuarioDTO>respuesta = usuarioService.registrarUsuario(regusuario, Rol.CLIENTE);

    //Comprobamos que el mensaje obtenido es el esperado
    assertEquals(REGISTRAR_USUARIO_OK,respuesta.getMessage());

  }

  //2. registrar usuario con email ya existente
  @Test
  public void registrar_usuario_con_email_ya_existente_Test(){
    //Creamos el usuario y la dirección
    Usuario u = new Usuario();
    u.setEmail("test@mail.com");
    u.setPassword("password");
    Direccion d = new Direccion();
    RegistrarUsuarioDTO regusuario = new RegistrarUsuarioDTO(u,d);
    System.out.println(regusuario.getU().getEmail());

    //Hacemos el "mocking"
    when(usuarioRepository.existsByEmail("test@mail.com")).thenReturn(true);

    //Ejecutamos el metodo a probar
    GenericResponse<UsuarioDTO>respuesta = usuarioService.registrarUsuario(regusuario, Rol.CLIENTE);

    //Comprobamos que el mensaje obtenido es el esperado
    assertEquals(this.EMAIL_YA_EXISTE,respuesta.getMessage());

  }

  //3. iniciar sesión con éxito
  @Test
  public void login_ok_Test(){
    Usuario usuario = new Usuario();
    usuario.setEmail("test@mail.com");
    usuario.setPassword("RBWaGm7bE2SBqBg9pkdkuIjqpZ8isPG");

    Mockito.when(usuarioRepository.findByEmail(usuario.getEmail()))
            .thenReturn(Optional.of(usuario));

    when(passwordEncoder.matches("admin","RBWaGm7bE2SBqBg9pkdkuIjqpZ8isPG"))
            .thenReturn(true);
    System.out.println(usuario.getEmail());

    //Llamamos al metodo
    GenericResponse<UsuarioDTO> response = usuarioService.login("test@mail.com","admin");

    System.out.println(response.getMessage());
    System.out.println(response.getRpta());

    //Verificar que fue OK
    assertEquals(this.LOGIN_OK,response.getMessage());

  }

  //4. iniciar sesión con una contraseña erronea
  @Test
  public void login_password_not_ok_Test(){
    Usuario usuario = new Usuario();
    usuario.setEmail("test@mail.com");
    usuario.setPassword("RBWaGm7bE2SBqBg9pkdkuIjqpZ8isPG");

    Mockito.when(usuarioRepository.findByEmail(usuario.getEmail()))
            .thenReturn(Optional.of(usuario));

    when(passwordEncoder.matches("admin","RBWaGm7bE2SBqBg9pkdkuIjqpZ8isPG"))
            .thenReturn(false);
    System.out.println(usuario.getEmail());

    //Llamamos al metodo
    GenericResponse<UsuarioDTO> response = usuarioService.login("test@mail.com","admin");

    System.out.println(response.getMessage());
    System.out.println(response.getRpta());

    //Verificar que fue OK
    assertEquals(this.LOGIN_PASS_NOT_OK,response.getMessage());
  }

  //5. iniciar sesión con un usuario que no existe
  @Test
  public void login_usuario_not_ok_Test(){
    String email = "testFail@mail.com";
    String pass = "RBWaGm7bE2SBqBg9pkdkuIjqpZ8isPG";
    Usuario usuario = new Usuario();
    usuario.setEmail(email);
    usuario.setPassword(pass);

    Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

    GenericResponse<UsuarioDTO> response = usuarioService.login(email,pass);

    System.out.println(response.getMessage());
    System.out.println(response.getRpta());

    assertEquals(this.USUARIO_NO_ENCONTRADO, response.getMessage());
  }


  //6. modificar los datos de un usuario
  @Test
  public void modificar_cliente_ok_Test(){

    Usuario usuario = crearUsuario();
    Usuario nuevoUsuario = modificarUsuario();


    when(usuarioRepository.findById(usuario.get_id())).thenReturn(Optional.of(usuario));
    when(usuarioRepository.existsByEmail("nuevo@mail.com")).thenReturn(false);

    GenericResponse<UsuarioDTO>respuesta = usuarioService.modificarCliente(usuario.get_id(),nuevoUsuario);
    System.out.println(respuesta.getMessage());
    assertEquals(this.MODIFICAR_CLIENTE_OK, respuesta.getMessage());

  }
  //7. modificar los datos de un usuario con un email ya existente
  @Test
  public void modificar_cliente_email_repetido_Test(){

    Usuario usuario = crearUsuario();
    Usuario nuevoUsuario = modificarUsuario();


    when(usuarioRepository.findById(usuario.get_id())).thenReturn(Optional.of(usuario));
    when(usuarioRepository.existsByEmail("nuevo@mail.com")).thenReturn(true);

    GenericResponse<UsuarioDTO>respuesta = usuarioService.modificarCliente(usuario.get_id(),nuevoUsuario);
    System.out.println(respuesta.getMessage());
    assertEquals(this.EMAIL_YA_EXISTE, respuesta.getMessage());

  }
  //8. modificar los datos de un usuario con datos no válidos
  @Test
  public void modificar_cliente_datos_not_ok_Test(){

    Usuario usuario = crearUsuario();
    Usuario nuevoUsuario = modificarUsuario();

    when(usuarioRepository.findById(usuario.get_id())).thenReturn(Optional.empty());

    GenericResponse<UsuarioDTO>respuesta = usuarioService.modificarCliente(usuario.get_id(),nuevoUsuario);
    System.out.println(respuesta.getMessage());
    assertEquals(this.USUARIO_NO_ENCONTRADO, respuesta.getMessage());

  }

  private Usuario modificarUsuario() {
    Usuario nuevoUsuario = new Usuario();
    nuevoUsuario.setEmail("nuevo@mail.com"); // Email nuevo (diferente)
    nuevoUsuario.setPassword("password");
    nuevoUsuario.setNombre("nombre");
    nuevoUsuario.setApellido("Apellido");
    nuevoUsuario.setTelefono("12341234");
    return nuevoUsuario;
  }

  private Usuario crearUsuario() {

    long id = 123;
    Usuario u = new Usuario();
    u.set_id(123);
    u.setEmail("test@mail.com");
    u.setPassword("password");
    u.setNombre("nombre");
    u.setApellido("Apellido");
    u.setTelefono("12341234");
    return u;

  }
}
