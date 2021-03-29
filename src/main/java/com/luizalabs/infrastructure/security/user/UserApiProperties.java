package com.luizalabs.infrastructure.security.user;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security")
public class UserApiProperties {

  private String jwtSecret;
  private List<UserApi> users = new ArrayList<>();
}
