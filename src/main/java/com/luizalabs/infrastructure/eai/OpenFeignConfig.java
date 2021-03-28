package com.luizalabs.infrastructure.eai;

import feign.Logger.Level;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignConfig {

  @Bean
  public Level feignLoggerLevel() {
    return Level.BASIC;
  }

  @Bean
  public ErrorDecoder errorDecoder() {
    return new FeignClientErrorDecoder();
  }

  @Bean
  public OkHttpClient client() {
    return new OkHttpClient();
  }
}
