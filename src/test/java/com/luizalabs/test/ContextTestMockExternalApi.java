package com.luizalabs.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.HystrixAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@ExtendWith(WiremockExtension.class)
@ImportAutoConfiguration({
  FeignAutoConfiguration.class,
  FeignAutoConfiguration.class,
  HystrixAutoConfiguration.class,
  HttpMessageConvertersAutoConfiguration.class
})
public abstract class ContextTestMockExternalApi {}
