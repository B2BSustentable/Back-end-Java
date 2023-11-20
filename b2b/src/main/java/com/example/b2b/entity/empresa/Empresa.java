package com.example.b2b.entity.empresa;

import com.example.b2b.dtos.empresa.RegisterRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

@Entity(name = "usuario")
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
public abstract class Empresa implements UserDetails {
    @Id
    @NotBlank
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nome;
    private String email;
    private String cnpj;
    private String senha;
    private String descricao;
    private LocalDateTime dataDeCriacao;
    @Enumerated(EnumType.STRING)
    private TipoPlanos tipoPlanos;

    public Empresa(RegisterRequestDTO data) {
        this.nome = data.nome();
        this.cnpj = data.cnpj();
        this.email = data.email();
        this.senha = data.senha();
        this.dataDeCriacao = data.dataDeCriacao();
        this.tipoPlanos = data.tipoPlanos();
    }

    public abstract ResponseEntity<String> getEmpresasPorGeoLocalizacao(String latitude, String longitude);

}
