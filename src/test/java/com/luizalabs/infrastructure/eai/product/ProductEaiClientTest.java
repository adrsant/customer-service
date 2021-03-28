package com.luizalabs.infrastructure.eai.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.luizalabs.infrastructure.configuration.ApiClientConfiguration;
import com.luizalabs.test.ContextTestMockExternalApi;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest(
    classes = {
      ProductEaiClient.class,
      ProductEaiClientFallbackFactory.class,
      ApiClientConfiguration.class
    })
class ProductEaiClientTest extends ContextTestMockExternalApi {

  @Autowired private ProductEaiClient client;

  @Test
  void shouldGetProductInfo() {
    UUID productId = UUID.fromString("1bf0f365-fbdd-4e21-9786-da459d78dd1f");

    var responseEntity = client.getProduct(productId);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void shouldDoNotGetProductInfoBecauseNotExists() {
    UUID productId = UUID.fromString("5d0eb672-77ab-4b94-b5a7-b1e7d63af239");

    var responseEntity = client.getProduct(productId);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
