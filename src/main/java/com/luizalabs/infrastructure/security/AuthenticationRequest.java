package com.luizalabs.infrastructure.security;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {

  private static final long serialVersionUID = 5926468583005150707L;

  private String username;
  private String password;
}
