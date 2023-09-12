package com.example.b2b.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity(name = "usuarioOuro ")
@Table(name = "usuario_ouro")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioOuro extends Usuario {
    private String tipoAssinatura = "Ouro";
    private int limiteDeProdutos;
    private double desconto;
    private boolean suporte24h;
    private String acessoVIP;


}
