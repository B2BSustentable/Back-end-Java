package com.example.b2b.dtos.endereco;

import com.example.b2b.entity.empresa.Empresa;

public record EnderecoResponseDTO(String rua,
                                  Integer numero,
                                  String bairro,
                                  String cidade,
                                  String estado,
                                  String pais,
                                  String cep,
                                  String latitude,
                                  String longitude,
                                  String nomeEmpresa,
                                  String cnpjEmpresa,
                                  String emailEmpresa
                                  ) {
}
