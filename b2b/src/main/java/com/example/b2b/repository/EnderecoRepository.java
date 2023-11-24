package com.example.b2b.repository;

import com.example.b2b.entity.endereco.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {

    Optional<Endereco> findByCep(String cep);

}
