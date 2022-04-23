package com.jacchm.project.config.security;

import com.jacchm.project.service.UserService;
import com.jacchm.project.util.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http,
                                                       final JwtTokenProvider jwtTokenProvider,
                                                       final ReactiveAuthenticationManager reactiveAuthenticationManager) {
    return http
        .csrf().disable()
        .cors().configurationSource(corsConfigurationSource())
        .and()
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .authenticationManager(reactiveAuthenticationManager)
        .authorizeExchange()
        .pathMatchers("/trainings/**").hasRole("USER")
        .pathMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
        .pathMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
        .anyExchange().permitAll()
        .and()
        .addFilterAt(new JwtTokenAuthenticationFilter(jwtTokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
        .build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:4200"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setMaxAge(3600L);
    configuration.applyPermitDefaultValues();
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public ReactiveAuthenticationManager authenticationManager(final UserService userService) {
    final UserDetailsRepositoryReactiveAuthenticationManager authManager =
        new UserDetailsRepositoryReactiveAuthenticationManager(userService);
    authManager.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    return authManager;
  }

  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

}
