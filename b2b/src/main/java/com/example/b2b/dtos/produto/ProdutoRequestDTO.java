package com.example.b2b.dtos.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record ProdutoRequestDTO(
        @NotBlank
        String nome,

        @NotNull
        Double preco
) {
}
