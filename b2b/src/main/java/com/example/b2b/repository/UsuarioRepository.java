package com.example.b2b.repository;

import com.example.b2b.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
        Optional<Usuario> findByCnpj(String cnpj);
        UserDetails findByEmail(String email);
}
