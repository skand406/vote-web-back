package com.example.votewebback;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http.csrf((csrf)->csrf.disable());
        http.authorizeHttpRequests((authorize)->authorize
                .requestMatchers("/**").permitAll());
        /*http.authorizeHttpRequests((authorize)->authorize
                .requestMatchers("/api/hello").permitAll())
                .formLogin(form->form.loginPage("/login").permitAll())
        ;*/

        return http.build();
    }
}