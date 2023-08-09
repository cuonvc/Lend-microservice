//package com.lender.apigateway.filter;
//
//import org.springframework.http.HttpMethod;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.function.Predicate;
//
//@Component
//public class RouteValidator {
//
//    public static final List<String> openApiEndpoints = List.of(
//            "/api/auth/sign-up",
//            "/api/auth/login",
//            "/eureka"
//    );
//
//    public Predicate<ServerHttpRequest> isSecured = request -> {
//        String path = request.getURI().getPath();
//        HttpMethod method = request.getMethod();
//
//        Map<String, List<String>> openApiEndpointsByMethod = Map.of(
//                HttpMethod.GET.name(), List.of("/api/product/category/view"),
//                HttpMethod.POST.name(), List.of("/api/auth/sign-up", "/api/auth/login")
//        );
//
//        List<String> allowedEndpoints = openApiEndpointsByMethod.getOrDefault(
//                method.name(),
//                List.of()
//        );
//
//        return allowedEndpoints.stream().noneMatch(path::startsWith);
//    };
//
////            request -> openApiEndpoints
////                    .stream()
////                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
//}
