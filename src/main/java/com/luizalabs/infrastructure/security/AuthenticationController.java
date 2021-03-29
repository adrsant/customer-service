package com.luizalabs.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final SecurityTokenService securityTokenService;
  private final MemoryUserDetailsService userDetailsService;

  @PostMapping(value = "/authenticate")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request)
      throws Exception {

    authenticate(request.getUsername(), request.getPassword());

    final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

    final String token = securityTokenService.generateToken(userDetails);

    return ResponseEntity.ok(new AuthenticationResponse(token));
  }

  private void authenticate(String username, String password) throws Exception {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }
}
