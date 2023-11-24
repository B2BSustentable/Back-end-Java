package com.example.b2b.repository;

import com.example.b2b.entity.plano.Plano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanoRepository extends JpaRepository<Plano, Integer> {

    Optional<Plano> findByIdPlano(int idPlano);

}
