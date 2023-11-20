package com.example.b2b.entity.empresa.roles;

import com.example.b2b.dtos.empresa.RegisterRequestDTO;
import com.example.b2b.entity.empresa.Empresa;
import com.example.b2b.entity.empresa.TipoPlanos;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity(name = "usuarioBronze")
@Table(name = "usuario_bronze")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmpresaBronze extends Empresa {
    private String tipoAssinatura = "Bronze";
    private int limiteDeProdutos;
    private double desconto;

    public EmpresaBronze(RegisterRequestDTO data) {
        super(data);
        this.limiteDeProdutos = data.limiteDeProdutos();
        this.desconto = data.desconto();
    }

    @Override
    public ResponseEntity<String> getEmpresasPorGeoLocalizacao(String latitude, String longitude){
     return null;
    };

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.getTipoPlanos().equals(TipoPlanos.USUARIO_BRONZE)) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getPassword() {
        return getSenha();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
