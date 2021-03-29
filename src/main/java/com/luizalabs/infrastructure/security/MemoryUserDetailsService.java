package com.luizalabs.infrastructure.security;

import com.luizalabs.infrastructure.security.user.UserApi;
import com.luizalabs.infrastructure.security.user.UserApiProperties;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemoryUserDetailsService implements UserDetailsService {

  private final UserApiProperties properties;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var userApiOpt = getUser(username);

    if (userApiOpt.isEmpty()) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
    var userApi = userApiOpt.get();

    return new User(userApi.getUsername(), userApi.getPassword(), userApi.getGrantedAuthorities());
  }

  private Optional<UserApi> getUser(String username) {
    return properties.getUsers().stream()
        .filter(userApi -> userApi.getUsername().equals(username))
        .findFirst();
  }
}
