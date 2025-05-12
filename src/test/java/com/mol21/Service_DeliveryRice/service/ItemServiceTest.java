package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.Carrito;
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
  public void add_items_to_carrito_ok_delete_item(){}

  @Test
  public void add_items_to_carrito_ok_modify_item(){}

  @Test
  public void add_items_to_carrito_ok(){}

  @Test
  public void add_items_to_carrito_fail_negative_cant(){}

  @Test
  public void add_items_to_carrito_fail_carrito_processed(){}

  @Test
  public void add_items_to_carrito_fail_carrito_not_valid(){}

  @Test
  public void modify_cant_ok(){}

  @Test
  public void modify_cant_ok_delete_item(){}

  @Test
  public void modify_cant_fail_carrito_processed(){}

  @Test
  public void modify_cant_fail_product_not_exist(){}

  @Test
  public void delete_item_ok(){}

  @Test
  public void delete_item_fail_carrito_processed(){}

  @Test
  public void delete_item_fail_product_not_exist(){}




















}
