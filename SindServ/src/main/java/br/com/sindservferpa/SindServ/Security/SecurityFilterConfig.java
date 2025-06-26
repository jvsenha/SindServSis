package br.com.sindservferpa.SindServ.Security;

import br.com.sindservferpa.SindServ.Util.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityFilterConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter; // Nosso filtro JWT

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desativa CSRF para APIs REST
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/login",
                                "/auth/register/func",
                                "/auth/register/assoc",
                                "/auth/register/assoc/lote",
                                "/public/**"
                        ).permitAll()
                        .anyRequest().authenticated() // As demais precisam de autenticação
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
                )
                // Adiciona nosso filtro JWT ANTES do filtro padrão de autenticação
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Aqui você pode adicionar um filtro de autenticação, como JWTFilter, se estiver usando tokens

        return http.build();
    }
}
