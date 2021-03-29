package com.luizalabs.infrastructure.security.user;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@Setter
public class UserApi {

  private String username;
  private String password;
  private List<String> authorities;

  public List<GrantedAuthority> getGrantedAuthorities() {
    return authorities.stream()
        .map(authority -> new SimpleGrantedAuthority(authority))
        .collect(Collectors.toList());
  }
}
