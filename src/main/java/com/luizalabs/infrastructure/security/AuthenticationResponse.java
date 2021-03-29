package com.luizalabs.infrastructure.security;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthenticationResponse implements Serializable {

  private static final long serialVersionUID = -8091879091924046844L;
  private final String token;
}
