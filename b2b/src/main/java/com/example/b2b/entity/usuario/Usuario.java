package com.example.b2b.entity.usuario;

import com.example.b2b.dtos.UsuarioDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.ResponseEntity;

@Entity(name = "usuario")
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String cnpj;
    private String senha;
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    public Usuario(UsuarioDTO data) {
        this.nome = data.nome();
        this.cnpj = data.cnpj();
        this.senha = data.senha();
        this.tipoUsuario = data.tipoUsuario();
    }

    public abstract ResponseEntity<String> fazerPostagem(String conteudo);
}
