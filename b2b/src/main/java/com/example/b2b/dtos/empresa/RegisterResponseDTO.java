package com.example.b2b.dtos.empresa;

import com.example.b2b.entity.empresa.TipoPlanos;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.br.CNPJ;

public record RegisterResponseDTO(String nomeEmpresa, @CNPJ String cnpj, java.time.LocalDateTime dataDeCriacao, @Email String email, TipoPlanos tipoPlanos, String descricao, String photo) {
}
