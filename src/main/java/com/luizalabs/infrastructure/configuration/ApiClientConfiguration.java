package com.luizalabs.infrastructure.configuration;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.luizalabs.infrastructure.eai")
@EnableCircuitBreaker
public class ApiClientConfiguration {}
