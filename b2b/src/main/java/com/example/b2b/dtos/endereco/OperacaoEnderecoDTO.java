package com.example.b2b.dtos.endereco;

import com.example.b2b.util.Operacao;

public record OperacaoEnderecoDTO(Operacao operacao, EnderecoRequestDTO endereco, String uIdEmpresa, String uIdEndereco) {
}
