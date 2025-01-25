package com.mol21.Service_DeliveryRice.model;

public enum EstadoPedido {
    PENDIENTE,//Usuario ha realizado el pago, pero no ha empezado a procesarse
    EN_PROCESO,//Sistema ha recibido y validado el pago. Se prepara el pedido y se envia.
    ENTREGADO,//El usuario ha recibido el producto
    CANCELADO //El pedido ha sido cancelado, por el usuario o por un problema con el pago.
}