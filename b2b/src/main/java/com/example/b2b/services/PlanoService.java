package com.example.b2b.services;

import com.example.b2b.dtos.plano.PlanoRequestDTO;
import com.example.b2b.entity.empresa.roles.EmpresaBasic;
import com.example.b2b.entity.empresa.roles.EmpresaCommon;
import com.example.b2b.entity.empresa.roles.EmpresaPremium;
import com.example.b2b.entity.plano.Plano;
import com.example.b2b.repository.PlanoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PlanoService {
    @Autowired
    private PlanoRepository planoRepository;

    public List<Plano> getTodosPlanos() {
        if (planoRepository.findAll().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum plano encontrado");
        } else {
            return planoRepository.findAll();
        }
    }

    public Plano cadastrarPlano(PlanoRequestDTO data) {
        Plano novoPlano = new Plano(data);

        novoPlano.setTipoPlanos(data.tipoPlanos());

        planoRepository.save(novoPlano);

        return (novoPlano);
    }

    public Plano getPlanoPorId(int id) {
        Optional<Plano> plano = planoRepository.findByIdPlano(id);

        if (plano.isPresent()) {
            return plano.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado");
        }
    }

    public Plano atualizarPlano(int id, PlanoRequestDTO data) {
        Optional<Plano> plano = planoRepository.findByIdPlano(id);

        if (plano.isPresent()) {
            plano.get().setTipoPlanos(data.tipoPlanos());
            plano.get().setValor(data.valor());
            plano.get().setQtdNegociantes(data.qtdNegociantes());
            plano.get().setLimiteProdutos(data.limiteProdutos());
            plano.get().setConsultasIlimitadas(data.consultasIlimitadas());
            plano.get().setAddFavoritos(data.addFavoritos());

            planoRepository.save(plano.get());

            return plano.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado");
        }
    }

    public void deletarPlano(int id) {
        Optional<Plano> plano = planoRepository.findByIdPlano(id);

        if (plano.isPresent()) {
            planoRepository.delete(plano.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado");
        }
    }

    public void configurarValoresBasicos(EmpresaBasic empresa) {
        if (planoRepository.findByIdPlano(1).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado");
        }
        empresa.setPlano(planoRepository.findByIdPlano(1).get());
        empresa.setQtdNegociantes(2);
        empresa.setLimiteDeProdutos(20);
    }

    public void configurarValoresCommon(EmpresaCommon empresa) {
        if (planoRepository.findByIdPlano(2).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado");
        }
        empresa.setPlano(planoRepository.findByIdPlano(2).get());
        empresa.setQtdNegociantes(3);
        empresa.setLimiteDeProdutos(50);
        empresa.setConsultasIlimitadas(true);
        empresa.setAddFavoritos(true);
    }

    public void configurarValoresPremium(EmpresaPremium empresa) {
        if (planoRepository.findByIdPlano(3).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado");
        }
        empresa.setPlano(planoRepository.findByIdPlano(3).get());
        empresa.setQtdNegociantes(5);
        empresa.setLimiteDeProdutos(100);
        empresa.setConsultasIlimitadas(true);
        empresa.setAddFavoritos(true);
    }
}
