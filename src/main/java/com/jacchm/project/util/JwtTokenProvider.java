package com.jacchm.project.util;

import com.jacchm.project.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import static java.util.stream.Collectors.joining;

@Slf4j
@Component
public class JwtTokenProvider {

  private static final String AUTHORITIES_KEY = "roles";
  private static final long ttl = 600000L;
  private static final String AES_ALGORITHM = "AES";
  private static final int KEY_SIZE = 256;

  private SecretKey secretKey;

  @PostConstruct
  protected void init() throws NoSuchAlgorithmException {
    final KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
    keyGenerator.init(KEY_SIZE);

    var secret = Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded());
    secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String createToken(final Authentication authentication) {
    final String username = authentication.getName();
    final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    final Claims claims = Jwts.claims().setSubject(username);
    claims.put(AUTHORITIES_KEY, authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));

    final Date now = new Date();
    final Date validity = new Date(now.getTime() + ttl);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public Authentication getAuthentication(final String token) {
    final Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
    final Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get(AUTHORITIES_KEY).toString());
    final User principal = new User(claims.getSubject(), "", authorities);
    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  public boolean validateToken(final String token) {
    try {
      final Jws<Claims> claims = Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      log.info("Invalid JWT token.");
      log.trace("Invalid JWT token trace.", e);
    }
    return false;
  }
}
