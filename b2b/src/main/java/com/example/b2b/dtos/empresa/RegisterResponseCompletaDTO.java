package com.example.b2b.dtos.empresa;

import com.example.b2b.entity.empresa.TipoPlanos;
import com.example.b2b.entity.endereco.Endereco;

import java.util.List;

public record RegisterResponseCompletaDTO(String id, String nomeEmpresa, String cnpj, java.time.LocalDateTime dataDeCriacao, String email, TipoPlanos tipoPlanos, String descricao, String photo, int qtdNegociantes, int limiteDeProdutos, boolean consultasIlimitadas, boolean addFavoritos, List<Endereco> enderecos) {
}
