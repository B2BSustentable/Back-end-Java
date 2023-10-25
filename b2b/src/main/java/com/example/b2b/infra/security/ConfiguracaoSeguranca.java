package com.example.b2b.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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

@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca {
    @Autowired
    FiltroDeSeguranca filtroDeSegurancaDaAutenticacao;

    private static final AntPathRequestMatcher[] URLS_PERMITIDAS_PARA_TODOS = {
            AntPathRequestMatcher.antMatcher("/h2-console/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/autenticacao/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/swagger-ui/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/v3/api-docs/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/produtos/**")
    };

    private static final AntPathRequestMatcher[] URLS_NECESSITAM_PERMISSAO = {
            AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/usuarios/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/ordenado/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/ordenado/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/produtos/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/produtos/**"),
            AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/produtos/**")
    };

    @Bean
    public SecurityFilterChain correnteFiltrosDeSeguranca(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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


}
