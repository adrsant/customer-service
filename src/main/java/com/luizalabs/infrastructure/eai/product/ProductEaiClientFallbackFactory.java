package com.luizalabs.infrastructure.eai.product;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class ProductEaiClientFallbackFactory implements FallbackFactory<ProductEaiClient> {

  @Override
  public ProductEaiClient create(Throwable cause) {
    return productId -> {
      log.error("integration error, cause : {}", cause.getMessage());
      return ResponseEntity.notFound().build();
    };
  }
}
