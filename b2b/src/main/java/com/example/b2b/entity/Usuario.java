package com.example.b2b.entity;

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

    public abstract ResponseEntity<String> fazerPostagem(String conteudo);
}
