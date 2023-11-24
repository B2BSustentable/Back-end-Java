package com.example.b2b.dtos.produto;

import jakarta.validation.constraints.NotBlank;


public record ProdutoRequestDTO(

        @NotBlank
        String nomeProduto,

        String categoria,

        String descricao,

        @NotBlank
        String codigoDeBarras
) {
}
