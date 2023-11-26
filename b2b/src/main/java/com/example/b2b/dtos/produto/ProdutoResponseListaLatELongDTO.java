package com.example.b2b.dtos.produto;

import jakarta.validation.constraints.NotBlank;

public record ProdutoResponseListaLatELongDTO(@NotBlank
                                              String nomeProduto,

                                              String categoria,

                                              String descricao,

                                              @NotBlank
                                              String codigoDeBarras,

                                              @NotBlank
                                              String nomeEmpresa,

                                              String latitude,

                                              String longitude)
{

}
