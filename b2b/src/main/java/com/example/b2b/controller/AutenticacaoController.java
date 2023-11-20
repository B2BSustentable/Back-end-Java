package com.example.b2b.controller;

import com.example.b2b.dtos.autenticacao.AutenticacaoDTO;
import com.example.b2b.dtos.autenticacao.LoginResponseDTO;
import com.example.b2b.dtos.empresa.RegisterRequestDTO;
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

            return ResponseEntity.ok(new LoginResponseDTO(token));
        }

        @PostMapping("/registrar")
        public ResponseEntity registrar(@RequestBody @Valid RegisterRequestDTO data) {
            if (this.empresaService.findByEmail(data.email()) != null) {
                return ResponseEntity.status(409).build();
            }

            String senhaEncriptada = new BCryptPasswordEncoder().encode(data.senha());
            Empresa empresa = empresaService.cadastrarUsuario(data.nome(), data.cnpj(), senhaEncriptada, LocalDateTime.now() ,data.email(), data.tipoPlanos(), data.tipoAssinatura(), data.limiteDeProdutos(), data.desconto(), data.suporte24h(), data.acessoVIP());

            RegisterResponseDTO registroResponse = new RegisterResponseDTO(empresa.getNome(), empresa.getCnpj(), empresa.getDataDeCriacao(), empresa.getEmail(), empresa.getTipoPlanos().toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(registroResponse);
    }
}
