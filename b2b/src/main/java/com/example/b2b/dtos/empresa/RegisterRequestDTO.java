package com.example.b2b.dtos.empresa;

import com.example.b2b.entity.empresa.TipoPlanos;
import com.example.b2b.entity.endereco.Endereco;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDateTime;
import java.util.List;

public record RegisterRequestDTO(@NotBlank String nomeEmpresa,@NotBlank String cnpj, @NotBlank @Email String email, @NotBlank String senha, String descricao, LocalDateTime dataDeCriacao, String photo, @NotNull TipoPlanos tipoPlanos, int qtdNegociantes, int limiteDeProdutos, boolean consultasIlimitadas, boolean addFavoritos, List<Endereco> enderecos) {
    }