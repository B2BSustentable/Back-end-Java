package com.example.b2b.entity.plano;

import com.example.b2b.dtos.plano.PlanoRequestDTO;
import com.example.b2b.entity.empresa.TipoPlanos;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "plano")
@Table(name = "plano")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="idPlano")
public class Plano {
    @Id
    private int idPlano;
    @Enumerated(EnumType.STRING)
    private TipoPlanos tipoPlanos;
    private Double valor;
    private int qtdNegociantes;
    private int limiteProdutos;
    private boolean consultasIlimitadas;
    private boolean addFavoritos;

    public Plano(PlanoRequestDTO data) {
        this.tipoPlanos = data.tipoPlanos();
        this.valor = data.valor();
        this.qtdNegociantes = data.qtdNegociantes();
        this.limiteProdutos = data.limiteProdutos();
        this.consultasIlimitadas = data.consultasIlimitadas();
        this.addFavoritos = data.addFavoritos();
    }
}
