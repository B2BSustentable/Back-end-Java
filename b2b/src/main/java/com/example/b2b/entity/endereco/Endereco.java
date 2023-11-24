package com.example.b2b.entity.endereco;

import com.example.b2b.dtos.endereco.EnderecoRequestDTO;
import com.example.b2b.entity.empresa.Empresa;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "endereco")
@Table(name = "endereco")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="uIdEndereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uIdEndereco;
    private String rua;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String pais;
    private String cep;
    private String latitude;
    private String longitude;
    @ManyToOne
    @JsonBackReference
    private Empresa empresa;

    public Endereco(EnderecoRequestDTO data) {
        this.rua = data.rua();
        this.numero = data.numero();
        this.bairro = data.bairro();
        this.cidade = data.cidade();
        this.estado = data.estado();
        this.pais = data.pais();
        this.cep = data.cep();
    }
}
