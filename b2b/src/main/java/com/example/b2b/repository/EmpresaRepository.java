package com.example.b2b.repository;

import com.example.b2b.entity.empresa.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
        Optional<Empresa> findByCnpj(String cnpj);
        UserDetails findByEmail(String email);
        Optional<Empresa> findByuIdEmpresa(String idEmpresa);
}
