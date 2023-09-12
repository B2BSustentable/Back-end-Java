package com.example.b2b.entity;

import com.example.b2b.dtos.UsuarioDTO;
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

    public UsuarioPrata(UsuarioDTO data) {
        super(data);
        this.limiteDeProdutos = data.limiteDeProdutos();
        this.desconto = data.desconto();
        this.suporte24h = data.suporte24h();
    }

    @Override
    public ResponseEntity<String> fazerPostagem(String conteudo) {
        if (conteudo != null && !conteudo.isEmpty()) {
            return ResponseEntity.status(200).body("Postagem do usuário Prata: " + conteudo);
        } else {
            return ResponseEntity.status(400).body("Conteúdo inválido.");
        }
    }


}