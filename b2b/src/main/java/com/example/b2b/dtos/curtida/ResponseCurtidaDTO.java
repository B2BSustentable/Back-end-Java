package com.example.b2b.dtos.curtida;

import com.example.b2b.entity.catalogo.Catalogo;
import com.example.b2b.entity.produto.Produto;
import com.example.b2b.entity.responsavel.Responsavel;

public record ResponseCurtidaDTO(
        Responsavel responsavel,
        Catalogo catalogo,
        Produto produto,
        boolean isCurtido
) {
}
