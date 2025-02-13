package com.amcom.ambev.order.model.dto;

import com.amcom.ambev.order.model.enumeration.BuyOrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;

@Builder
public record BuyOrderDTO(@NotNull BigInteger number,
                          @Valid @NotNull CustomerDTO customer,
                          @NotNull BuyOrderStatus status,
                          @Valid Set<ItemDTO> itens
                       ) implements Serializable {

}
