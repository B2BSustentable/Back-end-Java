package com.example.b2b.entity.empresa;

import com.example.b2b.dtos.empresa.RegisterRequestDTO;
import com.example.b2b.entity.catalogo.Catalogo;
import com.example.b2b.entity.endereco.Endereco;
import com.example.b2b.entity.plano.Plano;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "empresa")
@Table(name = "empresa")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="uIdEmpresa")
public abstract class Empresa implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uIdEmpresa;
    private String nomeEmpresa;
    @Column(unique = true)
    private String cnpj;
    @Column(unique = true)
    private String email;
    private String senha;
    private String descricao;
    private String photo;
    private LocalDateTime dataDeCriacao;
    @Enumerated(EnumType.STRING)
    private TipoPlanos tipoPlanos;
    @OneToOne
    private Plano plano;
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Endereco> endereco;

    public Empresa(RegisterRequestDTO registerRequestDTO) {
        this.nomeEmpresa = registerRequestDTO.nomeEmpresa();
        this.cnpj = registerRequestDTO.cnpj();
        this.email = registerRequestDTO.email();
        this.senha = registerRequestDTO.senha();
        this.descricao = registerRequestDTO.descricao();
        this.photo = registerRequestDTO.photo();
        this.dataDeCriacao = registerRequestDTO.dataDeCriacao();
        this.tipoPlanos = registerRequestDTO.tipoPlanos();
    }

    public abstract ResponseEntity<String> getEmpresasPorGeoLocalizacao(String latitude, String longitude);

}
