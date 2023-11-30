package com.example.b2b.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca {
    @Autowired
    FiltroDeSeguranca filtroDeSegurancaDaAutenticacao;

    private static final AntPathRequestMatcher[] URLS_PERMITIDAS_PARA_TODOS = {
            AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/h2-console/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/h2-console/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/autenticacao/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/swagger-ui/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/v3/api-docs/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/ordenado/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/ordenado/**"),
    };

    private static final AntPathRequestMatcher[] URLS_NECESSITAM_PERMISSAO = {
          AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/empresas/**"),
          AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/endereco/**"),
           AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/catalogo/**"),
           AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/ordenado/**"),
           AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/ordenado/**"),
          AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/produtos/**"),
          AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/produtos/**"),
           AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/produtos/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/responsavel/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/responsavel/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/responsavel/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/responsavel/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/produtos/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/produtos/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/produtos/**")
    };

    @Bean
    public SecurityFilterChain correnteFiltrosDeSeguranca(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(URLS_PERMITIDAS_PARA_TODOS).permitAll()
                        .requestMatchers(URLS_NECESSITAM_PERMISSAO).hasRole("ADMIN")
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(filtroDeSegurancaDaAutenticacao, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracao = new CorsConfiguration();
        configuracao.applyPermitDefaultValues();
        configuracao.setAllowedMethods(
                Arrays.asList(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.TRACE.name()));

        configuracao.setExposedHeaders(List.of(HttpHeaders.CONTENT_DISPOSITION));

        UrlBasedCorsConfigurationSource origem = new UrlBasedCorsConfigurationSource();
        origem.registerCorsConfiguration("/**", configuracao);

        return origem;
    }

}
