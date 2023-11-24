package com.example.b2b.dtos.catalogo;

import com.example.b2b.entity.produto.Produto;

import java.util.List;

public record CatalogoResponseDTO(List<Produto> produtos) {
}
