package com.example.b2b.repository.services;

import com.example.b2b.dtos.endereco.EnderecoRequestDTO;
import com.example.b2b.entity.endereco.Endereco;
import com.example.b2b.infra.ApiCepAberto;
import com.example.b2b.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EmpresaService empresaService;

    private ApiCepAberto apiCepAberto = new ApiCepAberto();

    public Endereco cadastrarEndereco(EnderecoRequestDTO endereco, String uIdEmpresa) {
        if (enderecoRepository.findByCep(endereco.cep()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Endereço já cadastrado");
        } else {
            Endereco novoEndereco = new Endereco();

            Optional<ApiCepAberto.Cep> cepData = apiCepAberto.searchByCep(endereco.cep());
            cepData.ifPresent(cep -> {
                // Atualizar os campos do novoEndereco com os dados retornados
                novoEndereco.setRua(cep.getLogradouro());
                novoEndereco.setNumero(endereco.numero());
                novoEndereco.setBairro(cep.getBairro());
                if (cep.getCidade() != null) {
                    novoEndereco.setCidade(cep.getCidade().getNome());
                }
                novoEndereco.setEstado(cep.getEstado().getSigla());
                novoEndereco.setPais("Brasil");
                novoEndereco.setCep(cep.getCep());
                novoEndereco.setLatitude(String.valueOf(cep.getLatitude()));
                novoEndereco.setLongitude(String.valueOf(cep.getLongitude()));
                // Associa o endereço à empresa
                novoEndereco.setEmpresa(empresaService.getEmpresaPorId(uIdEmpresa));
            });

            enderecoRepository.save(novoEndereco);

            return novoEndereco;
        }
    }

    public Endereco getEnderecoPorUId(String id) {
        if (enderecoRepository.findById(id).isPresent()) {
            return enderecoRepository.findById(id).get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado");
        }
    }

    public Endereco atualizarEndereco(String id, EnderecoRequestDTO endereco) {
        if (enderecoRepository.findById(id).isPresent()) {
            Endereco enderecoAtualizado = enderecoRepository.findById(id).get();

            Optional<ApiCepAberto.Cep> cepData = apiCepAberto.searchByCep(endereco.cep());
            cepData.ifPresent(cep -> {
                // Atualizar os campos do novoEndereco com os dados retornados
                enderecoAtualizado.setRua(cep.getLogradouro());
                enderecoAtualizado.setNumero(endereco.numero());
                enderecoAtualizado.setBairro(cep.getBairro());
                if (cep.getCidade() != null) {
                    enderecoAtualizado.setCidade(cep.getCidade().getNome());
                }
                enderecoAtualizado.setEstado(cep.getEstado().getSigla());
                enderecoAtualizado.setPais("Brasil");
                enderecoAtualizado.setCep(cep.getCep());
                enderecoAtualizado.setLatitude(String.valueOf(cep.getLatitude()));
                enderecoAtualizado.setLongitude(String.valueOf(cep.getLongitude()));
            });

            enderecoRepository.save(enderecoAtualizado);

            return enderecoAtualizado;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado");
        }
    }

    public void deletarEndereco(String id) {
        if (enderecoRepository.findById(id).isPresent()) {
            enderecoRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado");
        }
    }
}
