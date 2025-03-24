package com.spring.Library.Config;

import com.spring.Library.Services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(personDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     http    .authorizeHttpRequests((authz) -> authz
                     .requestMatchers("/people/**", "/books/**").hasRole("ADMIN")
                     .requestMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                     .anyRequest().hasAnyRole("USER", "ADMIN")
             )
             .formLogin((formLogin) -> formLogin.loginPage("/auth/login")
            .loginProcessingUrl("/process_login")
            .defaultSuccessUrl("/people")
            .failureUrl("/auth/login?error")
             )
             .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
