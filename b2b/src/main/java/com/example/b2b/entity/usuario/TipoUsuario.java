package com.example.b2b.entity.usuario;

public enum TipoUsuario {

    USUARIO_BRONZE("usuario_bronze"),

    USUARIO_PRATA("usuario_prata"),

    USUARIO_OURO("usuario_ouro");

    private String tipoUsuario;

    TipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }
}
