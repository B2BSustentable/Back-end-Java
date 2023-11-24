package com.example.b2b.dtos.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EnderecoRequestDTO(@NotBlank String rua,
                                 @NotNull @Positive Integer numero,
                                 @NotBlank String bairro,
                                 @NotBlank String cidade,
                                 @NotBlank String estado,
                                 @NotBlank String pais,
                                 @NotBlank String cep
) {
}
