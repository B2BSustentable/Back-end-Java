package com.example.b2b.dtos.plano;

import com.example.b2b.entity.empresa.TipoPlanos;

public record PlanoRequestDTO(
        TipoPlanos tipoPlanos,
        Double valor,
        int qtdNegociantes,
        int limiteProdutos,
        boolean consultasIlimitadas,
        boolean addFavoritos
) {
}
