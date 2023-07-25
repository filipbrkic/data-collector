package com.data.collector.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // using basic authentication
        http.httpBasic(Customizer.withDefaults());

        // disable cross site request forgery
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
