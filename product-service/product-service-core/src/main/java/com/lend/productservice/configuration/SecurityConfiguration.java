package com.lend.productservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/product/category/moderator/*").hasAnyAuthority("ROOT", "ADMIN", "MODERATOR")
                        .requestMatchers("/api/product/category/admin/*").hasAnyAuthority("ROOT", "ADMIN")
                        .requestMatchers("/api/product/category/root/*").hasAnyAuthority("ROOT")
                        .requestMatchers("/api/product/category/view/*").permitAll()
//                        .requestMatchers("/api/product/user/*").hasAnyAuthority("ROOT", "ADMIN", "MODERATOR", "USER")
                        .requestMatchers("/api/product/moderator/*").hasAnyAuthority("ROOT", "ADMIN", "MODERATOR")
                        .requestMatchers("/api/product/admin/*").hasAnyAuthority("ROOT", "ADMIN")
                        .requestMatchers("/api/product/root/*").hasAnyAuthority("ROOT")
                        .requestMatchers("/api/product/view/*", "/api/internal/**").permitAll()
                        .requestMatchers("/api/product/commodity/moderator/*").hasAnyAuthority("ROOT", "ADMIN", "MODERATOR")
                        .requestMatchers("/api/product/commodity/admin/*").hasAnyAuthority("ROOT", "ADMIN")
                        .requestMatchers("/api/product/commodity/root/*").hasAnyAuthority("ROOT")
                        .requestMatchers("/api/product/commodity/view/*").permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
