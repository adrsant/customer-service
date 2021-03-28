package com.luizalabs.infrastructure.eai.product;

import com.luizalabs.infrastructure.eai.OpenFeignConfig;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "challenge-api",
    url = "${integration.product-api.endpoint}",
    fallbackFactory = ProductEaiClientFallbackFactory.class,
    configuration = OpenFeignConfig.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public interface ProductEaiClient {

  @GetMapping(value = "/api/product/{productId}/")
  ResponseEntity getProduct(@PathVariable UUID productId);
}
