package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.Carrito;
import com.mol21.Service_DeliveryRice.model.DTO.CarritoDTO;
import com.mol21.Service_DeliveryRice.model.DTO.ItemDTO;
import com.mol21.Service_DeliveryRice.model.ItemCarrito;
import com.mol21.Service_DeliveryRice.model.Producto;
import com.mol21.Service_DeliveryRice.persistence.CarritoRepository;
import com.mol21.Service_DeliveryRice.persistence.ItemRepository;
import com.mol21.Service_DeliveryRice.persistence.ProductoRepository;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

  @Mock
  private ItemRepository itemRepository;
  @Mock
  private CarritoRepository carritoRepository;
  @Mock
  private ProductoRepository productoRepository;
  @InjectMocks
  private ItemService itemService;

  private final String MESSAGE_OK_LISTA_ITEMS="Se han obtenido la lista de items del carrito";
  private final String MESSAGE_OK_ITEM_DELETED="Item eliminado del carrito";
  private final String MESSAGE_OK_PRODUCT_MODIFIED="Producto modificado";
  private final String MESSAGE_OK_ADD_PRODUCT="Producto agregado al carrito";
  private final String MESSAGE_OK_CARRITO_UPDATE="Carrito actualizado con éxito";
  private final String MESSAGE_FAIL_ITEMS_NOT_FOUND="No se han encontrado items en ese carrito";
  private final String MESSAGE_FAIL_CARRITO_ALREADY_PROCESSED="Ese carrito ya ha sido procesado";
  private final String MESSAGE_FAIL_CARRITO_NOT_FOUND="No se ha encontrado ese carrito";
  private final String MESSAGE_FAIL_CANT_NEGATIVE="No puedes introducir una cantidad negativa";
  private final String MESSAGE_FAIL_CARRITO_PRODUCT_NOT_FOUND="Carrito o Producto no válidos";
  private final String MESSAGE_FAIL_PRODUCT_NOT_FOUND="No existe ese Producto";

  @Test
  public void listar_items_carrito_OK(){
    Carrito c = new Carrito();
    ItemCarrito i = new ItemCarrito();
    Producto p = new Producto();
    p.setPrecio(BigDecimal.TWO);
    p.setNombre("Arroz");
    p.setStock(20);
    i.setItem_id(21);
    i.setProducto(p);
    i.setCantidad(3);
    List<ItemCarrito> Items = new ArrayList<>();
    Items.add(i);
    c.setItemCarritoList(Items);
    System.out.println(c.Items());
    System.out.println(c.Items().size());
    when(carritoRepository.findById(c.getCarrito_id())).thenReturn(Optional.of(c));
    when(itemRepository.findByCarrito(c)).thenReturn(Items);
    GenericResponse<List<ItemDTO>> response = itemService.getItemsByCarrito(c.getCarrito_id());
    assertEquals(MESSAGE_OK_LISTA_ITEMS,response.getMessage());
  }

  @Test
  public void listar_items_carrito_fail_carrito_empty(){
    Carrito c = new Carrito();
    when(carritoRepository.findById(c.getCarrito_id())).thenReturn(Optional.of(c));
    GenericResponse<List<ItemDTO>> response = itemService.getItemsByCarrito(c.getCarrito_id());
    assertEquals(MESSAGE_FAIL_ITEMS_NOT_FOUND,response.getMessage());
  }

  @Test
  public void listar_items_carrito_fail_carrito_processed(){
    Carrito c = new Carrito();
    c.setProcesado(true);
    when(carritoRepository.findById(c.getCarrito_id())).thenReturn(Optional.of(c));
    GenericResponse<List<ItemDTO>> response = itemService.getItemsByCarrito(c.getCarrito_id());
    assertEquals(MESSAGE_FAIL_CARRITO_ALREADY_PROCESSED,response.getMessage());
  }

  @Test
  public void listar_items_carrito_fail_carrito_not_found(){
    Carrito c = new Carrito();
    when(carritoRepository.findById(c.getCarrito_id())).thenReturn(Optional.empty());
    GenericResponse<List<ItemDTO>> response = itemService.getItemsByCarrito(c.getCarrito_id());
    assertEquals(MESSAGE_FAIL_CARRITO_NOT_FOUND,response.getMessage());
  }

  @Test
  public void add_items_to_carrito_ok_delete_item(){
    Carrito c = new Carrito();
    Producto p = new Producto();
    p.setPrecio(new BigDecimal(23));
    c.setTotalProductos(2);
    c.setTotalPrecio(BigDecimal.TEN);
    ItemCarrito i = new ItemCarrito();
    i.setProducto(p);
    i.setSubTotal(BigDecimal.TWO);
    i.setCantidad(1);
    int cantidad = -3;
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.of(p));
    when(carritoRepository.findById(p.getId_product())).thenReturn(Optional.of(c));
    when(itemRepository.findByCarritoAndProducto(c,p)).thenReturn(Optional.of(i));
    GenericResponse<ItemDTO>response = itemService.addItemsToCarrito(c.getCarrito_id(),p.getId_product(),cantidad);
    assertEquals(MESSAGE_OK_ITEM_DELETED,response.getMessage());
  }

  @Test
  public void add_items_to_carrito_ok_modify_item(){
    Carrito c = new Carrito();
    Producto p = new Producto();
    p.setPrecio(new BigDecimal(23));
    c.setTotalProductos(2);
    c.setTotalPrecio(BigDecimal.TEN);
    ItemCarrito i = new ItemCarrito();
    i.setProducto(p);
    i.setSubTotal(BigDecimal.TWO);
    i.setCantidad(1);
    int cantidad = 3;
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.of(p));
    when(carritoRepository.findById(p.getId_product())).thenReturn(Optional.of(c));
    when(itemRepository.findByCarritoAndProducto(c,p)).thenReturn(Optional.of(i));
    GenericResponse<ItemDTO>response = itemService.addItemsToCarrito(c.getCarrito_id(),p.getId_product(),cantidad);
    assertEquals(MESSAGE_OK_PRODUCT_MODIFIED,response.getMessage());
  }

  @Test
  public void add_items_to_carrito_ok(){
    Carrito c = new Carrito();
    Producto p = new Producto();
    p.setPrecio(new BigDecimal(23));
    c.setTotalProductos(2);
    c.setTotalPrecio(BigDecimal.TEN);
    ItemCarrito i = new ItemCarrito();
    i.setProducto(p);
    i.setSubTotal(BigDecimal.TWO);
    i.setCantidad(1);
    int cantidad = 3;
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.of(p));
    when(carritoRepository.findById(p.getId_product())).thenReturn(Optional.of(c));
    when(itemRepository.findByCarritoAndProducto(c,p)).thenReturn(Optional.empty());
    GenericResponse<ItemDTO>response = itemService.addItemsToCarrito(c.getCarrito_id(),p.getId_product(),cantidad);
    assertEquals(MESSAGE_OK_ADD_PRODUCT,response.getMessage());
  }

  @Test
  public void add_items_to_carrito_fail_negative_cant(){
    Carrito c = new Carrito();
    Producto p = new Producto();
    p.setPrecio(new BigDecimal(23));
    c.setTotalProductos(2);
    c.setTotalPrecio(BigDecimal.TEN);
    ItemCarrito i = new ItemCarrito();
    i.setProducto(p);
    i.setSubTotal(BigDecimal.TWO);
    i.setCantidad(1);
    int cantidad = -3;
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.of(p));
    when(carritoRepository.findById(p.getId_product())).thenReturn(Optional.of(c));
    when(itemRepository.findByCarritoAndProducto(c,p)).thenReturn(Optional.empty());
    GenericResponse<ItemDTO>response = itemService.addItemsToCarrito(c.getCarrito_id(),p.getId_product(),cantidad);
    assertEquals(MESSAGE_FAIL_CANT_NEGATIVE,response.getMessage());
  }

  @Test
  public void add_items_to_carrito_fail_carrito_processed(){
    Carrito c = new Carrito();
    c.setProcesado(true);
    Producto p = new Producto();
    int cantidad = 3;
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.of(p));
    when(carritoRepository.findById(p.getId_product())).thenReturn(Optional.of(c));
    GenericResponse<ItemDTO>response = itemService.addItemsToCarrito(c.getCarrito_id(),p.getId_product(),cantidad);
    assertEquals(MESSAGE_FAIL_CARRITO_ALREADY_PROCESSED,response.getMessage());
  }

  @Test
  public void add_items_to_carrito_fail_carrito_not_valid(){
    Carrito c = new Carrito();
    Producto p = new Producto();
    int cantidad = 3;
    when(productoRepository.findById(p.getId_product())).thenReturn(Optional.empty());
    when(carritoRepository.findById(p.getId_product())).thenReturn(Optional.empty());
    GenericResponse<ItemDTO>response = itemService.addItemsToCarrito(c.getCarrito_id(),p.getId_product(),cantidad);
    assertEquals(MESSAGE_FAIL_CARRITO_PRODUCT_NOT_FOUND,response.getMessage());
  }

  @Test
  public void modify_cant_ok(){
    ItemCarrito i = new ItemCarrito();
    Producto p = new Producto();
    Carrito c = new Carrito();
    int cantidad = 3;
    p.setPrecio(BigDecimal.TWO);
    i.setProducto(p);
    i.setCantidad(1);
    c.setTotalPrecio(i.getSubTotal().multiply(BigDecimal.valueOf(i.getCantidad())));
    i.setCarrito(c);


    when(itemRepository.findById(i.getItem_id())).thenReturn(Optional.of(i));
    GenericResponse<ItemDTO>response = itemService.modificarCantidad(i.getItem_id(),cantidad);
    assertEquals(MESSAGE_OK_CARRITO_UPDATE,response.getMessage());
  }

  @Test
  public void modify_cant_ok_delete_item(){
    ItemCarrito i = new ItemCarrito();
    Producto p = new Producto();
    Carrito c = new Carrito();
    int cantidad = -3;
    p.setPrecio(BigDecimal.TWO);
    i.setProducto(p);
    i.setCantidad(1);
    c.setTotalPrecio(i.getSubTotal().multiply(BigDecimal.valueOf(i.getCantidad())));
    i.setCarrito(c);


    when(itemRepository.findById(i.getItem_id())).thenReturn(Optional.of(i));
    GenericResponse<ItemDTO>response = itemService.modificarCantidad(i.getItem_id(),cantidad);
    assertEquals(MESSAGE_OK_ITEM_DELETED,response.getMessage());
  }

  @Test
  public void modify_cant_fail_carrito_processed(){
    ItemCarrito i = new ItemCarrito();
    Carrito c = new Carrito();
    c.setProcesado(true);
    i.setCarrito(c);
    int cantidad = 3;

    when(itemRepository.findById(i.getItem_id())).thenReturn(Optional.of(i));
    GenericResponse<ItemDTO>response = itemService.modificarCantidad(i.getItem_id(),cantidad);
    assertEquals(MESSAGE_FAIL_CARRITO_ALREADY_PROCESSED,response.getMessage());
  }

  @Test
  public void modify_cant_fail_product_not_exist(){
    ItemCarrito i = new ItemCarrito();
    int cantidad = 3;
    when(itemRepository.findById(i.getItem_id())).thenReturn(Optional.empty());
    GenericResponse<ItemDTO>response = itemService.modificarCantidad(i.getItem_id(),cantidad);
    assertEquals(MESSAGE_FAIL_PRODUCT_NOT_FOUND,response.getMessage());
  }

  @Test
  public void delete_item_ok(){
    Producto p = new Producto();
    ItemCarrito i = new ItemCarrito();
    Carrito c = new Carrito();
    p.setPrecio(BigDecimal.TWO);
    i.setProducto(p);
    i.setCantidad(2);
    i.setSubTotal(p.getPrecio().multiply(BigDecimal.valueOf(i.getCantidad())));
    i.setCarrito(c);
    c.setTotalPrecio(p.getPrecio().multiply(BigDecimal.valueOf(i.getCantidad())));

    when(itemRepository.findById(i.getItem_id())).thenReturn(Optional.of(i));
    GenericResponse<CarritoDTO>response = itemService.eliminarItem(i.getItem_id());
    assertEquals(MESSAGE_OK_ITEM_DELETED,response.getMessage());
  }

  @Test
  public void delete_item_fail_carrito_processed(){
    ItemCarrito i = new ItemCarrito();
    Carrito c = new Carrito();
    c.setProcesado(true);
    i.setCarrito(c);

    when(itemRepository.findById(i.getItem_id())).thenReturn(Optional.of(i));
    GenericResponse<CarritoDTO>response = itemService.eliminarItem(i.getItem_id());
    assertEquals(MESSAGE_FAIL_CARRITO_ALREADY_PROCESSED,response.getMessage());
  }

  @Test
  public void delete_item_fail_product_not_exist(){
    ItemCarrito i = new ItemCarrito();
    when(itemRepository.findById(i.getItem_id())).thenReturn(Optional.empty());
    GenericResponse<CarritoDTO>response = itemService.eliminarItem(i.getItem_id());
    assertEquals(MESSAGE_FAIL_PRODUCT_NOT_FOUND,response.getMessage());
  }




















}
