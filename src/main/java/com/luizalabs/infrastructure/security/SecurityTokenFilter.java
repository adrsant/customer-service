package com.luizalabs.infrastructure.security;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class SecurityTokenFilter extends BasicAuthenticationFilter {

  private final MemoryUserDetailsService memoryUserDetailsService;
  private final SecurityTokenService securityTokenService;

  public SecurityTokenFilter(
      AuthenticationManager authenticationManager,
      AuthenticationEntryPoint authenticationEntryPoint,
      MemoryUserDetailsService memoryUserDetailsService,
      SecurityTokenService securityTokenService) {
    super(authenticationManager, authenticationEntryPoint);
    this.memoryUserDetailsService = memoryUserDetailsService;
    this.securityTokenService = securityTokenService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    final String requestTokenHeader = request.getHeader("Authorization");

    String username = null;
    String jwtToken = null;
    // JWT Token is in the form "Bearer token". Remove Bearer word and get
    // only the Token
    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      jwtToken = requestTokenHeader.substring(7);
      try {
        username = securityTokenService.getUsernameFromToken(jwtToken);
      } catch (IllegalArgumentException e) {
        log.error("Unable to get JWT Token");
      } catch (ExpiredJwtException e) {
        log.error("JWT Token has expired");
      }
    } else {
      log.warn("JWT Token does not begin with Bearer String");
    }

    // Once we get the token validate it.
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      UserDetails userDetails = this.memoryUserDetailsService.loadUserByUsername(username);

      // if token is valid configure Spring Security to manually set
      // authentication
      if (securityTokenService.validateToken(jwtToken, userDetails)) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }
    chain.doFilter(request, response);
  }
}
