package com.example.b2b.dtos.usuario;

public record RegisterResponseDTO(String nome, String cnpj, String senha, String email, String tipoUsuario) {

    public RegisterResponseDTO(RegisterDTO registerDTO){
        this(registerDTO.nome(), registerDTO.cnpj(), registerDTO.senha(), registerDTO.email(), registerDTO.tipoUsuario().toString());
    }

}
