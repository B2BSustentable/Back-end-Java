package com.example.b2b.entity.produto;

import com.example.b2b.dtos.produto.ProdutoRequestDTO;
import com.example.b2b.entity.catalogo.Catalogo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Table(name = "produto")
@Entity(name = "produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idProduto")
public class Produto {
    @Id
    @NotBlank
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idProduto;
    private String nomeProduto;
    private String categoria;
    private String descricao;
    private String codigoDeBarras;
    @JsonBackReference
    @ManyToOne
    private Catalogo catalogo;

    public Produto(ProdutoRequestDTO produtoRequestDTO) {
        this.nomeProduto = produtoRequestDTO.nomeProduto();
        this.categoria = produtoRequestDTO.categoria();
        this.descricao = produtoRequestDTO.descricao();
        this.codigoDeBarras = produtoRequestDTO.codigoDeBarras();
    }
}