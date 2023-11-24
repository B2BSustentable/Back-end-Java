package com.example.b2b.dtos.catalogo;

import com.example.b2b.entity.produto.Produto;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CatalogoRequestDTO(List<Produto> produtos, @NotBlank String idEmpresa) {
}
