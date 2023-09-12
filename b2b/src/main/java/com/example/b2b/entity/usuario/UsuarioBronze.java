package com.example.b2b.entity.usuario;

import com.example.b2b.dtos.UsuarioDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.http.ResponseEntity;

@Entity(name = "usuarioBronze")
@Table(name = "usuario_bronze")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioBronze extends Usuario {
    private String tipoAssinatura = "Bronze";
    private int limiteDeProdutos;
    private double desconto;

    public UsuarioBronze(UsuarioDTO data) {
        super(data);
        this.limiteDeProdutos = data.limiteDeProdutos();
        this.desconto = data.desconto();
    }

    @Override
    public ResponseEntity<String> fazerPostagem(String conteudo) {
        if (conteudo != null && !conteudo.isEmpty()) {
            return ResponseEntity.status(200).body("Postagem do usuário Bronze: " + conteudo);
        } else {
            return ResponseEntity.status(400).body("Conteúdo inválido.");
        }

    }
}
