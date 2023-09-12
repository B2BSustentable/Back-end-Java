package com.example.b2b.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.http.ResponseEntity;

@Entity(name = "usuarioPrata")
@Table(name = "usuario_prata")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioPrata extends Usuario {
    private String tipoAssinatura = "Prata";
    private int limiteDeProdutos;
    private double desconto;
    private boolean suporte24h;

    @Override
    public ResponseEntity<String> fazerPostagem(String conteudo) {
        if (conteudo != null && !conteudo.isEmpty()) {
            return ResponseEntity.status(200).body("Postagem do usuário Prata: " + conteudo);
        } else {
            return ResponseEntity.status(400).body("Conteúdo inválido.");
        }
    }


}