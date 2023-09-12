package com.example.b2b.entity.usuario;

import com.example.b2b.dtos.usuario.UsuarioDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.http.ResponseEntity;

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

    public UsuarioOuro(UsuarioDTO data) {
        super(data);
        this.limiteDeProdutos = data.limiteDeProdutos();
        this.desconto = data.desconto();
        this.suporte24h = data.suporte24h();
        this.acessoVIP = data.acessoVIP();
    }

    @Override
    public ResponseEntity<String> fazerPostagem(String conteudo) {
        if (conteudo != null && !conteudo.isEmpty()) {
            return ResponseEntity.status(200).body("Postagem do usuário Ouro: " + conteudo);
        } else {
            return ResponseEntity.status(400).body("Conteúdo inválido.");
        }
    }


}
