package com.tpp.rgrtpp.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/home", "/login", "/register", "/css/**", "/error", "/access-denied").permitAll()
                .requestMatchers("/cities/add", "/cities/edit/**", "/cities/delete/**").hasRole("ADMIN")
                .requestMatchers("/cities", "/shops", "/products").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/error")
                .defaultSuccessUrl("/home")
                .failureUrl("/login-error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied")
                .defaultAuthenticationEntryPointFor(
                    (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED),
                    new AntPathRequestMatcher("/api/**")
                )
            );
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
