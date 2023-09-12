package com.example.b2b.repository;

import com.example.b2b.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
        Usuario findByCnpj(String cnpj);
}
