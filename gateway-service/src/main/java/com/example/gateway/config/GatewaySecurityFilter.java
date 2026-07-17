package com.example.gateway.config;

import com.example.gateway.util.GatewayJwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class GatewaySecurityFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(GatewaySecurityFilter.class);

    private static final List<String> WHITELIST = List.of(
            "/api/user/login",
            "/api/user/register",
            "/api/attraction/**",
            "/api/hotel/**",
            "/api/route/search",
            "/api/route/hot",
            "/actuator/**"
    );

    private final GatewayJwtUtils jwtUtils;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public GatewaySecurityFilter(GatewayJwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        logger.info("GatewaySecurityFilter: Request path = {}", path);
        boolean isWhitelisted = isWhitelisted(path);
        logger.info("GatewaySecurityFilter: Path {} is whitelisted: {}", path, isWhitelisted);
        if (isWhitelisted) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "缺少或无效的 Token");
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return unauthorized(exchange, "Token 无效或已过期");
        }

        return chain.filter(exchange);
    }

    private boolean isWhitelisted(String path) {
        return WHITELIST.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = ("{\"success\":false,\"message\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
