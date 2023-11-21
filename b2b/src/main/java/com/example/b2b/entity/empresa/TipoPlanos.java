package com.example.b2b.entity.empresa;

import jakarta.persistence.OneToMany;

import java.util.List;

public enum TipoPlanos {

    USUARIO_BRONZE("usuario_bronze"),

    USUARIO_PRATA("usuario_prata"),

    USUARIO_OURO("usuario_ouro");

    private String tipoUsuario;

    TipoPlanos(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

}