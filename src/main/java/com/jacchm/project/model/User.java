package com.jacchm.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class User implements UserDetails {

  private static final String GRAND_AUTHORITIES_SEPARATOR = ",";

  @Id
  private Integer id;
  private String email;
  private String username;
  private String password;
  private String name;
  private String surname;
  private String authorities;

  public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
    this.username = username;
    this.password = password;
    this.authorities = authorities
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(GRAND_AUTHORITIES_SEPARATOR));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(authorities.split(GRAND_AUTHORITIES_SEPARATOR))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
