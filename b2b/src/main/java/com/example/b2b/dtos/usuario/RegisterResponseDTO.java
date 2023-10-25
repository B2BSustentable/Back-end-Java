package com.example.b2b.dtos.usuario;

public record RegisterResponseDTO(String nome, String cnpj, String senha, java.time.LocalDate dataDeCriacao, String email, String tipoUsuario) {
}
