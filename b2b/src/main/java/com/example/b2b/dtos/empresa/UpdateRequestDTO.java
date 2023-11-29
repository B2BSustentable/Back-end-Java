package com.example.b2b.dtos.empresa;

import com.example.b2b.entity.endereco.Endereco;

import java.util.List;

public record UpdateRequestDTO(String nomeEmpresa, String email, String senha, String descricao, String photo,List<Endereco> enderecos) {
}
