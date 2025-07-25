package com.sacpe.config;

import com.sacpe.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // Bean para cifrar contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Permite el acceso sin login a estas rutas
                .requestMatchers("/login", "/assets/**").permitAll()

                // Cualquier otra petición requiere estar autenticado redirigiéndose a la página de logi
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")             // URL de nuestra página de login personalizada
                .loginProcessingUrl("/login")    // URL que procesa el login (Spring Security se encarga)
                .defaultSuccessUrl("/", true) // A dónde ir después de un login exitoso
                .permitAll()
            )
            .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        );

        return http.build();
    }
}