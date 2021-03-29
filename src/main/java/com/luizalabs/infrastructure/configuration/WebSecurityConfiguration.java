package com.luizalabs.infrastructure.configuration;

import com.luizalabs.infrastructure.security.MemoryUserDetailsService;
import com.luizalabs.infrastructure.security.SecurityAuthenticationEntryPoint;
import com.luizalabs.infrastructure.security.SecurityTokenFilter;
import com.luizalabs.infrastructure.security.SecurityTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired private SecurityAuthenticationEntryPoint securityAuthenticationEntryPoint;

  @Autowired private UserDetailsService jwtUserDetailsService;
  @Autowired private MemoryUserDetailsService memoryUserDetailsService;
  @Autowired private SecurityTokenService securityTokenService;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf()
        .disable()
        .httpBasic()
        .disable()
        .cors()
        .and()
        .authorizeRequests()
        .antMatchers("/authenticate", "/v2/api-docs")
        .permitAll()
        .antMatchers(HttpMethod.OPTIONS, "/api/**")
        .hasAnyAuthority("READ_PRIVILEGE", "WRITE_PRIVILEGE")
        .antMatchers(HttpMethod.GET, "/api/**")
        .hasAnyAuthority("READ_PRIVILEGE", "WRITE_PRIVILEGE")
        .antMatchers("/api/**")
        .hasAuthority("WRITE_PRIVILEGE")
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(securityAuthenticationEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and();

    httpSecurity.addFilterBefore(
        new SecurityTokenFilter(
            authenticationManager(),
            securityAuthenticationEntryPoint,
            memoryUserDetailsService,
            securityTokenService),
        UsernamePasswordAuthenticationFilter.class);
  }
}
