package com.example.b2b.entity.curtida;

import com.example.b2b.entity.catalogo.Catalogo;
import com.example.b2b.entity.produto.Produto;
import com.example.b2b.entity.responsavel.Responsavel;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "curtida")
@Table(name = "curtida")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="uIdCurtida")
public class Curtida {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uIdCurtida;
    @ManyToOne
    @JoinColumn(name = "uid_responsavel")
    private Responsavel responsavelQueCurtiu;
    @ManyToOne
    @JoinColumn(name = "id_catalogo")
    private Catalogo catalogoDaEmpresa;
    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produto produtoCurtido;
    private boolean isCurtido;
}