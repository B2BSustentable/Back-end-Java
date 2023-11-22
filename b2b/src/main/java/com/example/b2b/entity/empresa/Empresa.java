package com.example.b2b.entity.empresa;

import com.example.b2b.dtos.empresa.RegisterRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

@Entity(name = "empresa")
@Table(name = "empresa")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
public abstract class Empresa implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nomeEmpresa;
    private String cnpj;
    private String email;
    private String senha;
    private String descricao;
    private String photo;
    private LocalDateTime dataDeCriacao;
    @Enumerated(EnumType.STRING)
    private TipoPlanos tipoPlanos;

    @OneToOne
    private Plano plano;

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
