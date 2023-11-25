package com.example.b2b.entity.mensagem;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "chat")
@Table(name = "chat")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of="idChat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idChat;
    private String nome;
    private String conteudo;
    private Date data;
}
