package com.mol21.Service_DeliveryRice.service;

import com.mol21.Service_DeliveryRice.model.*;
import com.mol21.Service_DeliveryRice.model.DTO.*;
import com.mol21.Service_DeliveryRice.persistence.*;
import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import static com.mol21.Service_DeliveryRice.utils.Global.*;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class CarritoService {
    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;
    private final DetalleRepository detalleRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public CarritoService(CarritoRepository carritoRepository, ProductoRepository productoRepository, UsuarioRepository usuarioRepository, DireccionRepository direccionRepository, DetalleRepository detalleRepository, PedidoRepository pedidoRepository) {
        this.carritoRepository = carritoRepository;
        this.usuarioRepository = usuarioRepository;
        this.direccionRepository = direccionRepository;
        this.detalleRepository = detalleRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    //Métodos del CarritoService:
    //1.- Crear nuevo Carrito ( se llamará cuando usuario descarte el anterior y cree el nuevo, cuando inicie una sesión y detecte que no hay ningún carrito o tras realizar un pedido
    //2.- Descartar un Carrito (se llamará cuando usuario cierra sesión)
    //3.- Obtener el ID del Carrito sin procesar
    //4.- Obtener el Carrito sin procesar
    //5.- Validar un Carrito (detecta si los items del pedido no exceden el stock actual de los productos, detecta que el carrito.
    //6.- Procesar Carrito
    //      Si Valores del mapa == 0, Carrito OK. Si Mapa vacío, carro vacío. Si mapa tiene algún valor != 0, excede stock
    //      Si null. No encuentra carrito

    //1.- Crear nuevo Carrito
    public GenericResponse<CarritoDTO> nuevoCarrito(long idUsuario) {
        Optional<Usuario> optU = usuarioRepository.findById(idUsuario);
        if (optU.isPresent()) {
            Usuario usuario = optU.get();
            //Tiene que saber si existe un carrito que no esté procesado porque no puede tener más de un carrito procesado
            Optional<Carrito> optC = carritoRepository.findByUsuarioAndProcesadoFalse(usuario);
            if (!optC.isPresent()) {
                Carrito carrito = new Carrito();
                carrito.setUsuario(optU.get());
                carrito.setTotalPrecio(BigDecimal.ZERO);
                carritoRepository.save(carrito);
                System.out.println("El id del carrito es: " + carrito.getCarrito_id());
                ArrayList<ItemDTO>listaVacia = new ArrayList<>();
                return new GenericResponse<>(
                        TIPO_DATA,
                        RPTA_OK,
                        "Se ha creado un nuevo carrito",
                        new CarritoDTO(carrito,listaVacia));
            }
            //Si tiene un carrito que no esté procesado no puede crear un nuevo carrito hasta que lo descarte o lo procese.
            else {
                System.out.println("EP!");
                Carrito carrito = optC.get();

                return new GenericResponse<>(
                        TIPO_DATA,
                        RPTA_WARNING,
                        "No se puede crear un nuevo carrito mientras tengas uno activo",
                        obtenerCarritoDTO(carrito)
                );
            }


        }
        //Si no se encuentra el ID
        else {
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No se ha encontrado el usuario",
                    null
            );
        }
    }

    //2.- Vaciar un carrito
    public GenericResponse<CarritoDTO> vaciarCarrito(long idUsuario) {
        Optional<Usuario> optU = usuarioRepository.findById(idUsuario);
        if (optU.isPresent()) {
            Usuario usuario = optU.get();
            Optional<Carrito> optC = carritoRepository.findByUsuarioAndProcesadoFalse(usuario);
            if (optC.isPresent()) {
                Carrito carrito = optC.get();
                if (!carrito.Items().isEmpty()) {
                    carrito.Items().clear();
                    carrito.setTotalProductos(0);
                    carrito.setTotalPrecio(BigDecimal.ZERO);
                    carritoRepository.save(carrito);
                    ArrayList<ItemDTO>listaVacia = new ArrayList<>();
                    return new GenericResponse<>(
                            TIPO_DATA,
                            RPTA_OK,
                            "Se ha vaciado el carrito",
                            new CarritoDTO(carrito, listaVacia)
                    );
                } else {
                    return new GenericResponse<>(
                            TIPO_DATA,
                            RPTA_WARNING,
                            "El carrito ya está vacío",
                            null
                    );
                }

            } else {
                return new GenericResponse<>(
                        TIPO_DATA,
                        RPTA_WARNING,
                        "No existe ese Carrito",
                        null
                );
            }
        } else {
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No se encuentra ese usuario",
                    null
            );
        }
    }

 //3.- Obtener el ID del carrito sin procesar
//    public GenericResponse<CarritoDTO> obtenerIdCarritoSinProcesar(long idUsuario) {
//        Optional<Usuario> optU = usuarioRepository.findById(idUsuario);
//        if (optU.isPresent()) {
//            Usuario usuario = optU.get();
//            Optional<Carrito> optC = carritoRepository.findByUsuarioAndProcesadoFalse(usuario);
//            if (optC.isPresent()) {
//                Carrito carritoSinProcesar = optC.get();
//                return new GenericResponse<>(
//                        TIPO_DATA,
//                        RPTA_OK,
//                        String.valueOf("carritoSinProcesar.getCarrito_id()"),
//                        obtenerCarritoDTO(carritoSinProcesar)
//                );
//
//            } else {
//                return new GenericResponse<>(
//                        TIPO_DATA,
//                        RPTA_WARNING,
//                        "Actualmente no tiene ningún carrito activo",
//                        null
//                );
//            }
//        } else {
//            return new GenericResponse<>(
//                    TIPO_DATA,
//                    RPTA_ERROR,
//                    "No se encuentra el usuario",
//                    null
//            );
//        }
//    }

    //4.- Obtener le carrito
    public GenericResponse<CarritoDTO> obtenerCarrito(long idUsuario) {
        //Se busca el usuario
        Optional<Usuario> optU = usuarioRepository.findById(idUsuario);
        if (optU.isPresent()) {
            Usuario usuario = optU.get();
            //Si el usurio existe se busca carrito sin procesar
            Optional<Carrito> optC = carritoRepository.findByUsuarioAndProcesadoFalse(usuario);
            if(optC.isPresent()){
                Carrito c = optC.get();
                return new GenericResponse<>(
                        TIPO_DATA,
                        RPTA_OK,
                        "Se ha obtenido el carrito sin procesar",
                        obtenerCarritoDTO(c)
                );
            } else{
                return new GenericResponse<>(
                        TIPO_DATA,
                        RPTA_OK,
                        "No tiene carrito activo",
                        null
                );
            }
        } else {
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No se encuentra el usuario",
                    null
            );
        }
    }


    //5.- Validar Carrito
    public GenericResponse<Map<ItemDTO, Integer>> validarCarrito(long idCarrito) {
        Optional<Carrito> optC = carritoRepository.findById(idCarrito);
        if (optC.isPresent()) {
            Carrito carrito = optC.get();
            carrito.Items();
            Map<ItemDTO, Integer> ItemsCarrito = new HashMap();
            for (ItemCarrito item : carrito.Items()) {
                int stockItem = item.getProducto().getStock();
                int cantidadItem = item.getCantidad();
                int exceso = cantidadItem - stockItem;
                if (exceso > 0) {
                    ItemsCarrito.put(new ItemDTO(item), exceso);
                } else {
                    ItemsCarrito.put(new ItemDTO(item), 0);
                }
            }
            if (!ItemsCarrito.isEmpty()) {
                boolean tieneExceso = false;
                //Recorremos los valores del mapa buscando numero diferente de cero (falta de stock)
                for (Integer exceso : ItemsCarrito.values()) {
                    if (exceso > 0) {
                        tieneExceso = true;
                        break;
                    }
                }
                if (tieneExceso) {
                    return new GenericResponse<>(
                            TIPO_DATA,
                            RPTA_WARNING,
                            "El carrito no puede procesarse porque uno o más elementos superan el stock permitido" +
                                    ", retírelos del carrito para continuar",
                            ItemsCarrito
                    );
                } else {
                    return new GenericResponse<>(
                            TIPO_DATA,
                            RPTA_OK,
                            "El carrito puede procesarse, no sobrepasa el stock de los productos",
                            ItemsCarrito);
                }
            } else {
                return new GenericResponse<>(
                        TIPO_DATA,
                        RPTA_WARNING,
                        "El carrito no puede procesarse porque está vacío",
                        Collections.emptyMap()
                );
            }
        } else {
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No se encuentra el Carrito introducido",
                    null
            );
        }
    }

    //7.- Procesar Pedido
    public GenericResponse<Object> procesarPedido(long idCarrito, MetodoPago metodoPago, long idDireccion) {
        Optional<Carrito> optC = carritoRepository.findById(idCarrito);
        Optional<Direccion> optD = direccionRepository.findById(idDireccion);
        if (!optD.isPresent()) {
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No se ha encontrado la dirección",
                    null
            );
        } else if (!optC.isPresent()) {
            return new GenericResponse<>(
                    TIPO_DATA,
                    RPTA_WARNING,
                    "No se ha encontrado el carrito",
                    null
            );
        } else {
            Carrito carrito = optC.get();
            Direccion direccion = optD.get();
            GenericResponse<Map<ItemDTO, Integer>> resultadoValidacion = validarCarrito(carrito.getCarrito_id());
            //SI validar no devuelve RPTA OK enviamos el resultado de validar
            if (resultadoValidacion.getRpta() != RPTA_OK) {
                return new GenericResponse<>(
                        TIPO_DATA,
                        RPTA_WARNING,
                        resultadoValidacion.getMessage(),
                        resultadoValidacion.getBody());
            } else {
                //Si Validar is RPTA_OK Carrito está validado
                //Creamos un nuevo pedido y actualizamos el stock del almacen
                Pedido pedido = new Pedido(carrito, direccion, metodoPago);
                //Cada Item se transforma en un detalle del pedido
                List<DetallePedido> detallePedidoList = new ArrayList<>();
                for (ItemCarrito item : carrito.Items()) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setPedido(pedido);
                    detalle.setCantidad(item.getCantidad());
                    detalle.setPrecioUnitario(item.getProducto().getPrecio());
                    detalle.setSubTotal(item.getSubTotal());
                    detalle.setProducto(item.getProducto());
                    detallePedidoList.add(detalle);
                    Producto p = detalle.getProducto();
                    p.setStock(p.getStock() - detalle.getCantidad());
                    productoRepository.save(p);
                }
                pedido.setDetalles(detallePedidoList);
                pedido.setTotalDetalles(pedido.getDetalles().size());
                pedidoRepository.save(pedido);
                detalleRepository.saveAll(detallePedidoList);
                //Procesamos el carrito y creamos uno nuevo
                carrito.setProcesado(true);
                carritoRepository.save(carrito);
                nuevoCarrito(carrito.getUsuario().get_id());

                List<DetalleDTO> listaDetallesDTOS = new ArrayList<>();
                for (DetallePedido detalle : detallePedidoList) {
                    listaDetallesDTOS.add(new DetalleDTO(detalle));
                }
                return new GenericResponse(
                        TIPO_DATA,
                        RPTA_OK,
                        "Se ha procedo el carrito y se ha creado el pedido",
                        new PedidoDTOCliente(pedido, listaDetallesDTOS)
                );
            }
        }
    }

    //Obtener CarritoDTO
    private CarritoDTO obtenerCarritoDTO(Carrito carrito) {
        ArrayList<ItemDTO> listaDTOS = new ArrayList<>();
        for (ItemCarrito i : carrito.Items()) {
            listaDTOS.add(new ItemDTO(i));
        }
        return new CarritoDTO(carrito, listaDTOS);
    }

}
