package com.amcom.ambev.order.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
public record ProductDTO(@NotBlank String code,
                         @NotBlank String description,
                         @NotNull BigDecimal price) implements Serializable {

}
