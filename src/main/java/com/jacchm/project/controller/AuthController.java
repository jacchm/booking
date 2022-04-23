package com.jacchm.project.controller;

import com.jacchm.project.controller.dto.AuthRequestDTO;
import com.jacchm.project.controller.dto.AuthResponseDTO;
import com.jacchm.project.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {

  private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
  private static final String SUCCESSFUL_LOGGING_MSG = """
      Successfully logged in user with username={}.""";

  private final ReactiveAuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;

  @PostMapping
  public Mono<ResponseEntity<AuthResponseDTO>> login(@Valid @RequestBody final AuthRequestDTO authRequest) {
    log.info("Logging user with credentials={}", authRequest);
    return Mono.just(authRequest)
        .flatMap(login -> authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())))
        .map(tokenProvider::createToken)
        .doOnSuccess(__ -> log.info(SUCCESSFUL_LOGGING_MSG, authRequest.getUsername()))
        .map(this::prepareResponse);
  }

  private ResponseEntity<AuthResponseDTO> prepareResponse(final String jwt) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_PREFIX + jwt);

    return new ResponseEntity<>(AuthResponseDTO.of(jwt), httpHeaders, HttpStatus.OK);
  }

}
