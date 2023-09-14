package com.example.b2b.entity.produto;

import com.example.b2b.dtos.produto.ProdutoRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Table(name = "produto")
@Entity(name = "produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Produto {
    @Id
    @NotBlank
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nome;
    private Double preco;

    public Produto(ProdutoRequestDTO data){
        this.preco = data.preco();
        this.nome = data.nome();
    }
}
