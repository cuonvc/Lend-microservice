//package com.lender.apigateway.filter;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Objects;
//
//@Component
////@RequiredArgsConstructor
//@Slf4j
//public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
//
//    public static class Config {
//
//    }
//
//    @Autowired
//    private RouteValidator routeValidator;
////    private final JwtUtil jwtUtil;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Value("${spring.cloud.gateway.routes[0].uri}")
//    private String authUri;
//
//    public AuthenticationFilter() {
//        super(Config.class);
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return (((exchange, chain) -> {
//            if (routeValidator.isSecured.test(exchange.getRequest())) {
//                //check header contains key or not
//                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                    throw new RuntimeException("Missing authorization header");
//                }
//
//                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
//                if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                    authHeader = authHeader.substring(7);
//                }
//
//                log.info(authUri);
//                boolean isValid = Boolean.TRUE.equals(restTemplate.getForObject(authUri + "/api/auth/gateway/valid?token=" + authHeader, Boolean.class));
////                if (isValid) {
////
////                }
//            }
//
//            return chain.filter(exchange);
//        }));
//    }
//}
