package com.example.b2b.entity.responsavel;

import com.example.b2b.dtos.responsavel.ResponsavelRegisterRequestDTO;
import com.example.b2b.entity.empresa.Empresa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "responsavel")
@Table(name = "responsavel")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="uIdResponsavel")
public class Responsavel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uIdResponsavel;
    private String nomeResponsavel;
    private String sobrenomeResponsavel;
    private String emailResponsavel;
    private String senhaResponsavel;
    private String  photoResponsavel;
    private boolean isLogado;
    private LocalDateTime dataDeCriacaoResponsavel;
    @ManyToOne
    private Empresa empresa;

    public Responsavel(ResponsavelRegisterRequestDTO data) {
        this.nomeResponsavel = data.nomeResponsavel();
        this.sobrenomeResponsavel = data.sobrenomeResponsavel();
        this.emailResponsavel = data.emailResponsavel();
        this.senhaResponsavel = data.senhaResponsavel();
        this.photoResponsavel = data.photoResponsavel();
        this.isLogado = false;
    }
}
