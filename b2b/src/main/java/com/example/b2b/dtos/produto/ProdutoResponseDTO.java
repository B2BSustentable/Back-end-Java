package com.example.b2b.dtos.produto;

import com.example.b2b.entity.produto.Produto;

public record ProdutoResponseDTO(String id, String nome, Double preco) {
    public ProdutoResponseDTO(Produto produto){
        this(produto.getId(), produto.getNome(), produto.getPreco());
    }
}
