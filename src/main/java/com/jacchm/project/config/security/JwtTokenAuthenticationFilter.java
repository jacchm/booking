package com.jacchm.project.config.security;

import com.jacchm.project.model.User;
import com.jacchm.project.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter implements WebFilter {

  private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
  public static final String USERNAME_HEADER = "username";

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    final String token = extractToken(exchange.getRequest());
    if (StringUtils.hasText(token) && this.jwtTokenProvider.validateToken(token)) {
      final Authentication authentication = this.jwtTokenProvider.getAuthentication(token);
      exchange
          .mutate()
          .request(addUsernameHeader(exchange, authentication))
          .build();
      return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }
    return chain.filter(exchange);
  }

  private ServerHttpRequest addUsernameHeader(final ServerWebExchange exchange, final Authentication authentication) {
    return exchange
        .getRequest()
        .mutate()
        .header(USERNAME_HEADER, ((User) authentication.getPrincipal()).getUsername())
        .build();
  }

  private String extractToken(ServerHttpRequest request) {
    final String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
