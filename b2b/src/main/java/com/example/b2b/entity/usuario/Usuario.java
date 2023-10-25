package com.example.b2b.entity.usuario;

import com.example.b2b.dtos.usuario.RegisterRequestDTO;
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
public abstract class Usuario implements UserDetails {
    @Id
    @NotBlank
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nome;
    private String email;
//    @CNPJ
    private String cnpj;
    private String senha;
    private LocalDateTime dataDeCriacao;
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    public Usuario(RegisterRequestDTO data) {
        this.nome = data.nome();
        this.cnpj = data.cnpj();
        this.email = data.email();
        this.senha = data.senha();
        this.dataDeCriacao = data.dataDeCriacao();
        this.tipoUsuario = data.tipoUsuario();
    }

    public abstract ResponseEntity<String> getEmpresasPorGeoLocalizacao(String latitude, String longitude);
}
