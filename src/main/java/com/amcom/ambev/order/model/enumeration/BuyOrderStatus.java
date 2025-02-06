package com.amcom.ambev.order.model.enumeration;

import lombok.Getter;

@Getter
public enum BuyOrderStatus {

    ORDER_PLACED("Pedido efetuado"),
    AWAITING_PAYMENT("Aguardando pagamento"),
    PAID_ORDER("Pedido pago"),
    INVOICED("Pedido faturado"),
    ORDER_SEPARATION("Pedido em separacao"),
    ORDER_SENT("Pedido enviado"),
    ORDER_DELIVERY("Pedido entregue"),
    ORDER_READY_PICKUP("Pedido pronto para retirada"),
    ORDER_CANCELED("Pedido cancelado");

    private final String displayName;

    BuyOrderStatus(String displayName) {
        this.displayName = displayName;
    }

}
