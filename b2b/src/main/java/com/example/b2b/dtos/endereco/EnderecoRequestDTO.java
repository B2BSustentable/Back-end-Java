package com.example.b2b.dtos.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EnderecoRequestDTO(String rua,
                                 @Positive Integer numero,
                                 String bairro,
                                 String cidade,
                                 String estado,
                                 String pais,
                                 @NotBlank String cep
) {
}
