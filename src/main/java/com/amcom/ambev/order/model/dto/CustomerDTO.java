package com.amcom.ambev.order.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record CustomerDTO(@NotBlank String name,
                          @NotBlank String email,
                          @NotBlank @Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11}|\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}|\\d{14})$",
                                  message = "Invalid CPF or CNPJ format"
                          ) String document ,
                          String phoneNumber
                ) implements Serializable {

}
