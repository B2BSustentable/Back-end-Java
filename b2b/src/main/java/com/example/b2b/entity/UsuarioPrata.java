package com.example.b2b.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity(name = "usuarioPrata")
@Table(name = "usuario_prata")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
public class UsuarioPrata extends Usuario {
    private String tipoAssinatura = "Prata";
    private int limiteDeProdutos;
    private double desconto;
    private boolean suporte24h;


}