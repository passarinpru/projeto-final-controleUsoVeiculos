package br.senai.sistema.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança do Sistema de Controle de Veículos.
 *
 * SENAI - Encontro 07
 *
 * Perfis:
 *
 * ADMIN:
 * - Cadastrar, consultar, editar e excluir funcionários.
 * - Cadastrar, consultar, editar e excluir veículos.
 * - Registrar saídas e devoluções.
 * - Consultar histórico de utilizações.
 * - Excluir registros de utilização.
 * - Acessar o console do banco H2.
 *
 * OPERADOR:
 * - Consultar funcionários e veículos.
 * - Registrar saídas e devoluções.
 * - Consultar histórico de utilizações.
 * - Não pode alterar cadastros ou excluir registros.
 *
 * As regras são verificadas de cima para baixo.
 */
@Configuration
public class SecurityConfig {

    @Value("${app.seguranca.ativa:false}")
    private boolean ativa;

    @Bean
    public SecurityFilterChain filtroDeSeguranca(HttpSecurity http) throws Exception {

        if (ativa) {

            http.authorizeHttpRequests(auth -> auth

                // Arquivos públicos e tela de login
                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/login",
                    "/diagnostico"
                ).permitAll()

                // Console H2: somente administrador
                .requestMatchers(PathRequest.toH2Console())
                .hasRole("ADMIN")

                // Gerenciamento de usuários: somente administrador
                .requestMatchers("/usuarios/**")
                .hasRole("ADMIN")

                // Consulta de funcionários: qualquer usuário logado
                .requestMatchers(HttpMethod.GET, "/funcionarios")
                .authenticated()

                // Cadastro, edição e exclusão de funcionários
                .requestMatchers("/funcionarios/**")
                .hasRole("ADMIN")

                // Consulta de veículos: qualquer usuário logado
                .requestMatchers(HttpMethod.GET, "/veiculos")
                .authenticated()

                // Cadastro, edição e exclusão de veículos
                .requestMatchers("/veiculos/**")
                .hasRole("ADMIN")

                // Exclusão de utilizações: somente administrador
                .requestMatchers("/utilizacoes/*/excluir")
                .hasRole("ADMIN")

                // Utilizações: administrador e operador
                .requestMatchers("/utilizacoes/**")
                .hasAnyRole("ADMIN", "OPERADOR")

                // Todas as demais páginas exigem login
                .anyRequest()
                .authenticated()
            );

        } else {

            // Segurança desligada: acesso liberado
            http.authorizeHttpRequests(auth ->
                auth.anyRequest().permitAll()
            );
        }

        http
            // Tela de login personalizada
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )

            // Encerrar sessão
            .logout(logout -> logout
                .logoutSuccessUrl("/login?saiu")
                .permitAll()
            )

            // Configurações necessárias para o console H2
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(PathRequest.toH2Console())
            )

            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }

    /**
     * Criptografia das senhas utilizando BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}