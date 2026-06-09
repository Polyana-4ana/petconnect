package com.example.petconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .headers(headers ->
                        headers.frameOptions(
                                frameOptions ->
                                        frameOptions.sameOrigin()))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**")
                        .permitAll()

                        .requestMatchers(
                                "/h2-console/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/pets/**")
                        .permitAll()

                        .requestMatchers("/pets/**")
                        .authenticated()

                        .requestMatchers("/adotantes/**")
                        .authenticated()

                        .requestMatchers("/adocoes/**")
                        .authenticated()

                        .requestMatchers("/relatorios/**")
                        .authenticated()

                        .anyRequest()
                        .authenticated())

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}