package com.example.b2b.dtos.usuario;

public record RegisterResponseDTO(String nome, String cnpj, java.time.LocalDateTime dataDeCriacao, String email, String tipoUsuario) {
}
