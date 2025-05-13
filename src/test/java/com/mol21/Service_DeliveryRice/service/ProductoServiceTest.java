package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.CategoriaProducto;
import com.mol21.Service_DeliveryRice.model.DTO.ProductoDTO;
import com.mol21.Service_DeliveryRice.model.Producto;
import com.mol21.Service_DeliveryRice.persistence.ProductoRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {
  @Mock
  private ProductoRepository productoRepository;
  @InjectMocks
  private ProductoService productoService;

  final String MESSAGE_OK_GET_PRODUCTO = "Se ha devuelvo el producto";
  final String MESSAGE_OK_GET_LIST_PRODUCTS = "Se han obtenido la lista de productos por categoria";
  final String MESSAGE_OK_NEW_PRODUCT = "Producto ingresado con éxito";
  final String MESSAGE_OK_PRODUCTO_MODIFY = "Producto modifcado con éxito";
  final String MESSAGE_OK_PRODUCTO_DELETE = "Producto eliminado con éxito";
  final String MESSAGE_FAIL_PRODUCTO_ALREADY_EXISTS = "Ese producto ya existe";
  final String MESSAGE_FAIL_NO_PRODUCTOS_CATEGORY = "No se han encontrado productos en esa categoria";
  final String MESSAGE_FAIL_PRODUCTO_NOT_FOUND = "No se encuentra el producto";
  final String MESSAGE_FAIL_CAMPO_NO_VALIDO = "Campo no válido";


  @Test
  public void obtener_producto_ok() {
    Producto p = new Producto();
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.of(p));
    GenericResponse<ProductoDTO> response = productoService.obtenerProducto(p.getId_product());
    assertEquals(MESSAGE_OK_GET_PRODUCTO, response.getMessage());
  }

  @Test
  public void obtener_producto_fail() {
    Producto p = new Producto();
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.empty());
    GenericResponse<ProductoDTO> response = productoService.obtenerProducto(p.getId_product());
    assertEquals(MESSAGE_FAIL_PRODUCTO_NOT_FOUND, response.getMessage());
  }

  @Test
  public void obtener_productos_categoria_fail() {
    List<Producto> productos = new ArrayList<>();
    when(productoRepository.findByCategoriaProducto(CategoriaProducto.ARROZ)).thenReturn(productos);
    GenericResponse<List> response = productoService.getProductByCategory(CategoriaProducto.ARROZ);
    assertEquals(MESSAGE_FAIL_NO_PRODUCTOS_CATEGORY, response.getMessage());
  }

  @Test
  public void obtener_productos_categoria_ok() {
    Producto p = new Producto();
    List<Producto> productos = new ArrayList<>();
    productos.add(p);
    when(productoRepository.findByCategoriaProducto(CategoriaProducto.ARROZ)).thenReturn(productos);
    GenericResponse<List> response = productoService.getProductByCategory(CategoriaProducto.ARROZ);
    assertEquals(MESSAGE_OK_GET_LIST_PRODUCTS, response.getMessage());
  }

  @Test
  public void registrar_producto_fail() {
    Producto p = new Producto();
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.of(p));
    GenericResponse<ProductoDTO> response = productoService.registrarProducto(p);
    assertEquals(MESSAGE_FAIL_PRODUCTO_ALREADY_EXISTS, response.getMessage());
  }

  @Test
  public void registrar_producto_ok() {
    Producto p = new Producto();
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.empty());
    GenericResponse<ProductoDTO> response = productoService.registrarProducto(p);
    assertEquals(MESSAGE_OK_NEW_PRODUCT, response.getMessage());
  }

  @Test
  public void modificar_producto_fail() {
    Producto p = new Producto();
    Map<String, Object> updates = new HashMap<>();
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.empty());
    GenericResponse<ProductoDTO> response = productoService.modificarProducto(p.getId_product(), updates);
    assertEquals(MESSAGE_FAIL_PRODUCTO_NOT_FOUND, response.getMessage());
  }

  @Test
  public void modificar_producto_ok() {
    Producto p = new Producto();
    Map<String, Object> updates = new HashMap<>();
    updates.put("nombre", "Arroz");
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.of(p));
    GenericResponse<ProductoDTO> response = productoService.modificarProducto(p.getId_product(), updates);
    assertEquals(MESSAGE_OK_PRODUCTO_MODIFY, response.getMessage());
  }

  @Test
  public void modificar_producto_fail_campo_no_valido() {
    Producto p = new Producto();
    Map<String, Object> updates = new HashMap<>();
    updates.put("numero", "Arroz");
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.of(p));
    GenericResponse<ProductoDTO> response = productoService.modificarProducto(p.getId_product(), updates);
    assertEquals(MESSAGE_FAIL_CAMPO_NO_VALIDO, response.getMessage());
  }

  @Test
  public void eliminar_producto_ok() {
    Producto p = new Producto();
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.of(p));
    GenericResponse<Void> response = productoService.eliminarProducto(p.getId_product());
    assertEquals(MESSAGE_OK_PRODUCTO_DELETE,response.getMessage());
  }

  @Test
  public void eliminar_producto_fail() {
    Producto p = new Producto();
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.empty());
    GenericResponse<Void> response = productoService.eliminarProducto(p.getId_product());
    assertEquals(MESSAGE_FAIL_PRODUCTO_NOT_FOUND,response.getMessage());
  }
}
