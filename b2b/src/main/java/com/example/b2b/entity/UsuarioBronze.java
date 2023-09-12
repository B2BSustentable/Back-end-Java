package com.example.b2b.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity(name = "usuarioBronze")
@Table(name = "usuario_bronze")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
public class UsuarioBronze extends Usuario {
    private String tipoAssinatura = "Bronze";
    private int limiteDeProdutos;
    private double desconto;

}
