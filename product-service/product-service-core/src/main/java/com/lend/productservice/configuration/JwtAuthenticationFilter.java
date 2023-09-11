package com.lend.productservice.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromToken(request);
        log.info("Checking - {}", token);

        if (StringUtils.hasText(token)) {
            ResponseEntity<CustomUserDetail> authResponse = restTemplate.getForEntity(
                    "http://AUTH-SERVICE/api/auth/internal/check?token=" + token,
                    CustomUserDetail.class
            );

            if (authResponse.getStatusCode().is2xxSuccessful()) {
                CustomUserDetail userDetails = authResponse.getBody();
                assert userDetails != null;
                SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(userDetails, userDetails.getGrantedAuthorities()));
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken buildAuthenticationToken(CustomUserDetail userDetails, List<String> authorityList) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = authorityList.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new UsernamePasswordAuthenticationToken(userDetails, null, simpleGrantedAuthorities);
    }

    private String getJwtFromToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }
}
