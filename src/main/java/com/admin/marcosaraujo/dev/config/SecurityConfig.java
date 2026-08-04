package com.admin.marcosaraujo.dev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ⚡ CORREÇÃO DO ERRO 403: Ignora a validação CSRF para chamadas de API feitas via Fetch/JS
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                .authorizeHttpRequests(auth -> auth
                        // Libera arquivos estáticos e a página de login
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login").permitAll()
                        // Garante que as rotas de API continuem exigindo usuário logado
                        .requestMatchers("/api/**").authenticated()
                        // Exige autenticação para qualquer outra rota do admin
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")               // Rota da nossa página de login customizada
                        .defaultSuccessUrl("/", true)      // Redireciona para a home/dashboard após logar
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    // Encoder seguro para senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Usuário em memória para o desenvolvimento da Sprint 2
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username("marcos")
                .password(encoder.encode("admin123")) // Altere para a sua senha de preferência
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}