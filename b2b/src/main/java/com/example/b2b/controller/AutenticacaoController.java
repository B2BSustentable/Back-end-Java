package com.example.b2b.controller;

import com.example.b2b.dtos.autenticacao.AutenticacaoDTO;
import com.example.b2b.dtos.autenticacao.LoginResponseDTO;
import com.example.b2b.dtos.usuario.RegisterDTO;
import com.example.b2b.dtos.usuario.RegisterResponseDTO;
import com.example.b2b.entity.usuario.Usuario;
import com.example.b2b.infra.security.TokenService;
import com.example.b2b.repository.UsuarioRepository;
import com.example.b2b.services.UsuarioService;
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

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    //http://localhost:8080/swagger-ui/index.html -> Para consulta da Documentação Swagger
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

        @PostMapping("/login")
        public ResponseEntity login(@RequestBody @Valid AutenticacaoDTO data) {
            var senhaUsuario = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
            var autenticacao = authenticationManager.authenticate(senhaUsuario);

            var token = tokenService.gerarToken((Usuario) autenticacao.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        }

        @PostMapping("/registrar")
        public ResponseEntity registrar(@RequestBody @Valid RegisterDTO data) {
            if (this.usuarioService.findByEmail(data.email()) != null) {
                return ResponseEntity.status(409).build();
            }

            String senhaEncriptada = new BCryptPasswordEncoder().encode(data.senha());
            Usuario usuario = usuarioService.cadastrarUsuario(data.nome(), data.cnpj(), senhaEncriptada, data.email(), data.tipoUsuario(), data.tipoAssinatura(), data.limiteDeProdutos(), data.desconto(), data.suporte24h(), data.acessoVIP());

            RegisterResponseDTO registroResponse = new RegisterResponseDTO(usuario.getNome(), usuario.getCnpj(), usuario.getSenha(), usuario.getEmail(), usuario.getTipoUsuario().toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(registroResponse);
    }
}
