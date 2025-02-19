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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import static com.mol21.Service_DeliveryRice.utils.Global.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    public ItemService(ItemRepository itemRepository, CarritoRepository carritoRepo, ProductoRepository productoRepository) {
        this.itemRepository = itemRepository;
        this.carritoRepository = carritoRepo;
        this.productoRepository = productoRepository;
    }
    /*
     * Métodos del Servicio de ItemCarrito:
     * agregarItemAlCarrito(carritoid, producto, cantidad)
     * actualizarItem(ItemCarrito)
     * eliminarItem(id)
     * vaciarCarrito(carrito)
     * listarItemsByCarrito(carritoId)*/

    //1.- Listar Items de un Carrito
    public GenericResponse<List<ItemDTO>> getItemsByCarrito(long carritoId) {
        Optional<Carrito> carritoOpt = carritoRepository.findById(carritoId);
        if (carritoOpt.isPresent()) {
            if(!carritoOpt.get().isProcesado()){
                ArrayList<ItemDTO> itemsDTOS = new ArrayList<>();
                List<ItemCarrito> items = itemRepository.findByCarrito(carritoOpt.get());
                if (!items.isEmpty()) {
                    for (ItemCarrito item : items) {
                        itemsDTOS.add(new ItemDTO(item));
                    }
                    return new GenericResponse<>(
                            TIPO_DATA,
                            RPTA_OK,
                            "Se han obtenido la lista de items del carrito",
                            itemsDTOS);

                } else {
                    return new GenericResponse<>(
                            TIPO_DATA,
                            RPTA_WARNING,
                            "No se han encontrado items en ese carrito",
                            null
                    );
                }
            }else{
                return new GenericResponse<>(
                        TIPO_DATA,
                        RPTA_WARNING,
                        "Ese carrito ya ha sido procesado",
                        null);
            }

        } else {
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No se ha encontrado ese carrito",
                    null);
        }

    }
    //2.- Agregar Item a Carrito
    public GenericResponse<CarritoDTO> addItemsToCarrito(long carritoId, long productoID, int cantidad) {
        Optional<Producto> optP = productoRepository.findById(productoID);
        Optional<Carrito> optC = carritoRepository.findById(carritoId);
        //Nos cercioramos que existan ese producto y ese carrito
        if (optP.isPresent() && optC.isPresent()) {
            if(!optC.get().isProcesado()){
                Carrito carrito = optC.get();
                Producto producto = optP.get();
                //Buscamos el item que tenga ese producto en ese carrito
                Optional<ItemCarrito> optI = itemRepository.findByCarritoAndProducto(carrito, producto);
                ItemCarrito item;
                //Si ya existe un producto en ese carrito modificamos su cantidad
                if (optI.isPresent()) {
                    item = optI.get();
                    int nuevaCantidad = item.getCantidad() + cantidad;
                    //Si la nueva cantidad es menor o igual a cero, elimina el Item del Carrito
                    if (nuevaCantidad <= 0) {
                        deleteItem(item, carrito);
                        CarritoDTO carritoDTO = obtenerCarritoDTO(carrito);
                        return new GenericResponse<>(
                                TIPO_DATA,
                                RPTA_OK,
                                "Se ha eliminado el ItemCarrito ",
                                carritoDTO
                        );
                    }
                    //Si la cantidad no menor que Cero actualiza la cantidad,el precio del Carrito y la cantidad de productos.
                    else {
                        item.setCantidad(nuevaCantidad);
                        carrito.setTotalProductos(carrito.getTotalProductos() + cantidad);
                        carrito.setTotalPrecio(carrito.getTotalPrecio().add(producto.getPrecio().multiply(BigDecimal.valueOf(cantidad))));
                        //Actualizamos BD
                        carritoRepository.save(carrito);
                        itemRepository.save(item);
                        CarritoDTO carritoDTO = obtenerCarritoDTO(carrito);
                        return new GenericResponse<>(
                                TIPO_DATA,
                                RPTA_OK,
                                "Producto modificado",
                                carritoDTO
                        );
                    }
                }
                //Si no existe, creamos un nuevo Item
                else {
                    if (cantidad > 0) {
                        item = new ItemCarrito(cantidad, carrito, producto);
                        itemRepository.save(item);

                        //Actualizamos el carrito cada vez que añadimos un item a la BD y el stock del Producto
                        carrito.setTotalProductos(carrito.getTotalProductos() + cantidad);
                        carrito.setTotalPrecio(carrito.getTotalPrecio().add(item.getSubTotal()));
                        carritoRepository.save(carrito);
                        CarritoDTO carritoDTO = obtenerCarritoDTO(carrito);
                        //Devolvemos la respuesta con el itemDTO
                        return new GenericResponse<>(
                                TIPO_DATA,
                                RPTA_OK,
                                "Producto agregado al carrito",
                                carritoDTO
                        );


                    } else {
                        return new GenericResponse<>(
                                TIPO_DATA,
                                RPTA_WARNING,
                                "No puedes introducir una cantidad negativa",
                                null);
                    }
                }
            }else{
                return new GenericResponse<>(
                        TIPO_DATA,
                        RPTA_WARNING,
                        "Ese carrito ya ha sido procesado",
                        null);
            }

        } else {
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "Carrito o Producto no válidos",
                    null
            );
        }
    }
    //3.- ActualizarItem
    public GenericResponse<CarritoDTO> modificarCantidad(long itemId, int cantidad) {
        Optional<ItemCarrito> optI = itemRepository.findById(itemId);

        if (optI.isPresent()) {
            ItemCarrito item = optI.get();
            Carrito carrito = item.getCarrito();
            if(!carrito.isProcesado()){
                int nuevaCantidad = item.getCantidad() + cantidad;

                if (nuevaCantidad > 0) {

                    //Guardamos la nueva cantidad y subtotal
                    BigDecimal antiguoValor = item.getSubTotal();
                    BigDecimal totalCarrito = carrito.getTotalPrecio();
                    BigDecimal nuevoValor = item.getProducto().getPrecio().multiply(BigDecimal.valueOf(nuevaCantidad));

                    //Eliminamos el Subtotal del Item anterior y añadimos el nuevo Subtotal
                    carrito.setTotalPrecio(totalCarrito.subtract(antiguoValor));
                    totalCarrito = carrito.getTotalPrecio();
                    carrito.setTotalPrecio(totalCarrito.add(nuevoValor));

                    //Añadimos la nueva cantidad al carrito y al item
                    carrito.setTotalProductos(carrito.getTotalProductos() + cantidad);
                    item.setCantidad(nuevaCantidad);
                    //Guardamos los cambios
                    itemRepository.save(item);
                    carritoRepository.save(carrito);

                    //Creamos lista de items del carrito y creamos lista de ItemsDTO
                    CarritoDTO carritoDTO = obtenerCarritoDTO(carrito);


                    return new GenericResponse<>(
                            TIPO_DATA,
                            RPTA_OK,
                            "Carrito actualizado con éxito",
                            carritoDTO
                    );

                } else {
                    deleteItem(item, carrito);

                    //Creamos lista de items del carrito y creamos lista de ItemsDTO
                    CarritoDTO carritoDTO = obtenerCarritoDTO(carrito);

                    return new GenericResponse<>(
                            TIPO_DATA,
                            RPTA_WARNING,
                            "Item eliminado del carrito",
                            carritoDTO
                    );
                }
            }else{
                return new GenericResponse<>(
                        TIPO_DATA,
                        RPTA_WARNING,
                        "Ese carrito ya ha sido procesado",
                        null
                );
            }

        } else {
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No existe ese Producto",
                    null
            );
        }
    }
    //4.- EliminarItem
    public GenericResponse<CarritoDTO> eliminarItem(long itemId) {
        Optional<ItemCarrito> optI = itemRepository.findById(itemId);
        if (optI.isPresent()) {
            //Si existe lo eliminamos y actualizamos el carrito
            ItemCarrito item = optI.get();
            Carrito carrito = item.getCarrito();
            if(!carrito.isProcesado()){
                ItemDTO itemDTO = new ItemDTO(item);
                deleteItem(item, carrito);

                CarritoDTO carritoDTO = obtenerCarritoDTO(carrito);

                return new GenericResponse<>(
                        TIPO_DATA,
                        RPTA_OK,
                        "Producto eliminado del carrito: " + itemDTO.getNombreProducto(),
                        carritoDTO
                );
            }else{
                return new GenericResponse<>(
                        TIPO_DATA,
                        RPTA_WARNING,
                        "Ese carrito ya ha sido procesado",
                        null
                );
            }

        } else {
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No existe ese Producto",
                    null
            );
        }
    }
//    //5.- VaciarCarrito
//    public GenericResponse<CarritoDTO> vaciarCarrito(long carritoId) {
//        Optional<Carrito> optC = carritoRepository.findById(carritoId);
//        if (optC.isPresent()) {
//            Carrito carrito = optC.get();
//            if (!carrito.Items().isEmpty()) {
//                carrito.Items().clear();
//                carrito.setTotalProductos(0);
//                carrito.setTotalPrecio(BigDecimal.ZERO);
//                itemRepository.deleteByCarrito(carrito);
//                carritoRepository.save(carrito);
//                return new GenericResponse<>(
//                        TIPO_DATA,
//                        RPTA_OK,
//                        "Se ha vaciado el carrito",
//                        new CarritoDTO(carrito)
//                );
//            } else {
//                return new GenericResponse<>(
//                        TIPO_DATA,
//                        RPTA_WARNING,
//                        "El carrito ya está vacío",
//                        null
//                );
//            }
//
//        } else {
//            return new GenericResponse<>(
//                    TIPO_DATA,
//                    RPTA_WARNING,
//                    "No existe ese Producto",
//                    null
//            );
//        }
//    }
    //Funcion para obtener una lista de ItemsDTOS a partir del carrito(
    private CarritoDTO obtenerCarritoDTO(Carrito carrito) {
        ArrayList<ItemDTO> listaDTOS = new ArrayList<>();
        List<ItemCarrito> items = itemRepository.findByCarrito(carrito);
        for (ItemCarrito i : items) {
            listaDTOS.add(new ItemDTO(i));
        }
        return new CarritoDTO(carrito,listaDTOS);
    }
    //Funcion para eliminar un Item
    private void deleteItem(ItemCarrito item, Carrito carrito) {
        carrito.Items().remove(item);
        carrito.setTotalProductos(carrito.getTotalProductos() - item.getCantidad());
        carrito.setTotalPrecio(carrito.getTotalPrecio().subtract(item.getSubTotal()));
        itemRepository.deleteById(item.getItem_id());
        carritoRepository.save(carrito);
    }



}
