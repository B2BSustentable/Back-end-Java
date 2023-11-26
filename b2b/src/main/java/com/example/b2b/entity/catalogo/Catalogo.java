package com.example.b2b.entity.catalogo;

import com.example.b2b.entity.empresa.Empresa;
import com.example.b2b.entity.produto.Produto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "catalogo")
@Entity(name = "catalogo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idCatalogo")
public class Catalogo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idCatalogo;

    @JsonManagedReference
    @OneToMany(mappedBy = "catalogo")
    private List<Produto> produtos;

    @JsonBackReference
    @OneToOne
    private Empresa empresa;
}
