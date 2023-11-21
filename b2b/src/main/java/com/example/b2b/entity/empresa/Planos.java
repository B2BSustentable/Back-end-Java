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
public class Planos {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private int idPlano;
    private Double valor;
    private int qtdConsultas;
    private int qtdNegociantes;
    private boolean addFavoritos;

    public Planos(TipoPlanos tipoPlanos) {
        this.idPlano = idPlano;
        this.valor = valor;
        this.qtdConsultas = qtdConsultas;
        this.qtdNegociantes = qtdNegociantes;
        this.addFavoritos = addFavoritos;
    }

    @OneToMany
    private List<Empresa> empresas;
}
