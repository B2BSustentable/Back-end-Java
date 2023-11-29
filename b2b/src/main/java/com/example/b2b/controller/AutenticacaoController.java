package com.example.b2b.controller;

import com.example.b2b.dtos.autenticacao.AutenticacaoDTO;
import com.example.b2b.dtos.autenticacao.LoginResponseDTO;
import com.example.b2b.dtos.empresa.RegisterRequestDTO;
import com.example.b2b.dtos.empresa.RegisterResponseCompletaDTO;
import com.example.b2b.dtos.empresa.RegisterResponseDTO;
import com.example.b2b.entity.empresa.Empresa;
import com.example.b2b.infra.security.TokenService;
import com.example.b2b.services.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    //http://localhost:8080/swagger-ui/index.html -> Para consulta da Documentação Swagger
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private TokenService tokenService;

        @PostMapping("/login")
        public ResponseEntity login(@RequestBody @Valid AutenticacaoDTO data) {
            var senhaUsuario = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
            var autenticacao = authenticationManager.authenticate(senhaUsuario);

            var token = tokenService.gerarToken((Empresa) autenticacao.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token, ((Empresa) autenticacao.getPrincipal()).getUIdEmpresa()));
        }

        @PostMapping("/registrar")
        public ResponseEntity registrar(@RequestBody @Valid RegisterRequestDTO data) {

            String senhaEncriptada = new BCryptPasswordEncoder().encode(data.senha());
            Empresa empresaEntity = empresaService.cadastrarEmpresa(new RegisterRequestDTO(data.nomeEmpresa(), data.cnpj(), data.email(), senhaEncriptada, data.descricao(), LocalDateTime.now(), data.photo(), data.tipoPlanos(), data.qtdNegociantes(), data.limiteDeProdutos(), data.consultasIlimitadas(), data.addFavoritos(), data.enderecos()));

            RegisterResponseDTO registroResponse = new RegisterResponseDTO(empresaEntity.getNomeEmpresa(), empresaEntity.getCnpj(), empresaEntity.getDataDeCriacao(), empresaEntity.getEmail(), empresaEntity.getTipoPlanos(), empresaEntity.getDescricao(), empresaEntity.getPhoto(), empresaEntity.getEndereco());
            RegisterResponseCompletaDTO registroResponseCompleta = new RegisterResponseCompletaDTO(empresaEntity.getUIdEmpresa(), empresaEntity.getNomeEmpresa(), empresaEntity.getCnpj(), empresaEntity.getDataDeCriacao(), empresaEntity.getEmail(), empresaEntity.getTipoPlanos(), empresaEntity.getDescricao(), empresaEntity.getPhoto(), empresaEntity.getPlano().getQtdNegociantes(), empresaEntity.getPlano().getLimiteProdutos(), empresaEntity.getPlano().isConsultasIlimitadas(), empresaEntity.getPlano().isAddFavoritos(), empresaEntity.getEndereco());

            return ResponseEntity.status(HttpStatus.CREATED).body(registroResponseCompleta);
    }
}
