package com.example.b2b.entity.empresa;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "planos")
@Table(name = "planos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="idPlano")
public class Plano {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPlano;
    private TipoPlanos tipoPlanos;
    private Double valor;
    private int qtdConsultas;
    private int qtdNegociantes;
    private boolean addFavoritos;

    public Plano(TipoPlanos tipoPlanos, Double valor, int qtdConsultas, int qtdNegociantes, boolean addFavoritos) {
        this.tipoPlanos = tipoPlanos;
        this.valor = valor;
        this.qtdConsultas = qtdConsultas;
        this.qtdNegociantes = qtdNegociantes;
        this.addFavoritos = addFavoritos;
    }
}
