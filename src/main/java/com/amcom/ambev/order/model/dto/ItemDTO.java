package com.amcom.ambev.order.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
public record ItemDTO(@NotBlank String productCode,
                      @NotNull @Min(value = 1) Integer amount,
                      @NotNull BigDecimal fullDiscount) implements Serializable {

}



