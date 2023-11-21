package com.example.b2b.entity.empresa;

public enum TipoPlanos {

    EMPRESA_BASIC("empresa_basic"),

    EMPRESA_COMMON("empresa_common"),

    EMPRESA_PREMIUM("empresa_premium");

    private String tipoPlanoEmpresa;

    TipoPlanos(String tipoPlanoEmpresa) {
        this.tipoPlanoEmpresa = tipoPlanoEmpresa;
    }

    public String getTipoPlano() {
        return tipoPlanoEmpresa;
    }

}