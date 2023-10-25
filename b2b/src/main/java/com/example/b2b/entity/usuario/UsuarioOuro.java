package com.example.b2b.entity.usuario;

import com.example.b2b.dtos.usuario.RegisterRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity(name = "usuarioOuro ")
@Table(name = "usuario_ouro")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioOuro extends Usuario {
    private String tipoAssinatura = "Ouro";
    private int limiteDeProdutos;
    private double desconto;
    private boolean suporte24h;
    private String acessoVIP;

    public UsuarioOuro(RegisterRequestDTO data) {
        super(data);
        this.limiteDeProdutos = data.limiteDeProdutos();
        this.desconto = data.desconto();
        this.suporte24h = data.suporte24h();
        this.acessoVIP = data.acessoVIP();
    }

    @Override
    public ResponseEntity<String> getEmpresasPorGeoLocalizacao(String latitude, String longitude){
        return null;
    };


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.getTipoUsuario().equals(TipoUsuario.USUARIO_OURO)) {
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
