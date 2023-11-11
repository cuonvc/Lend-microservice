package com.lend.authservice.config;

import com.lend.authservice.config.jwt.JwtAuthenticationEntryPoint;
import com.lend.authservice.config.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public PasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(handle -> handle.authenticationEntryPoint(authenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/sign-up", "/api/auth/login",
                                "/api/auth/gateway/valid", "/api/auth/active-account",
                                "/api/auth/password/*", "/api/auth/validate",
                                "/api/auth/internal/*").permitAll()
                        .requestMatchers("/api/auth/moderator/*").hasAnyAuthority("ROOT", "ADMIN", "MODERATOR")
                        .requestMatchers("/api/auth/admin/*").hasAnyAuthority("ROOT", "ADMIN")
                        .requestMatchers("/api/auth/root/*").hasAnyAuthority("ROOT")
                        .requestMatchers(HttpMethod.GET, "/api/auth/account/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/token/renew").permitAll()
                        .anyRequest()
                        .authenticated())
                        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }
}
