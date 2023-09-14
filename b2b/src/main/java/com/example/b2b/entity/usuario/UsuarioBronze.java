package com.example.b2b.entity.usuario;

import com.example.b2b.dtos.usuario.RegisterDTO;
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
public class UsuarioBronze extends Usuario {
    private String tipoAssinatura = "Bronze";
    private int limiteDeProdutos;
    private double desconto;

    public UsuarioBronze(RegisterDTO data) {
        super(data);
        this.limiteDeProdutos = data.limiteDeProdutos();
        this.desconto = data.desconto();
    }

    @Override
    public ResponseEntity<String> fazerPostagem(String conteudo) {
        if (conteudo != null && !conteudo.isEmpty()) {
            return ResponseEntity.status(200).body("Postagem do usuário Bronze: " + conteudo);
        } else {
            return ResponseEntity.status(400).body("Conteúdo inválido.");
        }

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.getTipoUsuario().equals(TipoUsuario.USUARIO_PRATA)) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // Todos os usuários têm a role "ROLE_USER"
//
//        if (this.getTipoUsuario().equals(TipoUsuario.USUARIO_BRONZE)) {
//            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        }
//
//        return authorities;
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
