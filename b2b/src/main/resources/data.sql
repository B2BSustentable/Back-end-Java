CREATE TABLE IF NOT EXISTS plano (
    id_plano INTEGER GENERATED BY DEFAULT AS IDENTITY,
    tipo_planos VARCHAR(255),
    valor FLOAT(50),
    qtd_negociantes INTEGER,
    limite_produtos INTEGER,
    consultas_ilimitadas BOOLEAN,
    add_favoritos BOOLEAN,
    PRIMARY KEY (id_plano)
    );

INSERT INTO plano (id_plano, tipo_planos, valor, qtd_negociantes, limite_produtos, consultas_ilimitadas, add_favoritos) VALUES
(1, 'EMPRESA_BASIC', 50.0, 2, 20, false, false),
(2, 'EMPRESA_COMMON', 100.0, 3, 50, true, true),
(3, 'EMPRESA_PREMIUM', 200.0, 5, 100, true, true);