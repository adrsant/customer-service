package com.luizalabs.interaction.favority;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.infrastructure.configuration.CacheConfiguration;
import com.luizalabs.infrastructure.eai.product.ProductEaiClient;
import com.luizalabs.interaction.favority.command.AddFavoriteCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ExistProductValidationTest {

  @Mock private ProductEaiClient productEaiClient;
  @Mock private CacheManager cacheManager;
  @InjectMocks private ExistProductValidation validation;

  @Test
  void shouldDoNothingBecauseExistProductAndNotCached() {
    var command =
        AddFavoriteCommand.builder()
            .customerId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();
    Cache cache = mock(Cache.class);
    given(cacheManager.getCache(CacheConfiguration.PRODUCT_CACHE_NAME)).willReturn(cache);
    given(cache.get(command.getProductId())).willReturn(null);
    given(productEaiClient.getProduct(command.getProductId()))
        .willReturn(ResponseEntity.ok().build());

    var throwableAssert = assertThatCode(() -> validation.valid(command));

    throwableAssert.doesNotThrowAnyException();
    then(cache).should().put(command.getProductId(), true);
  }

  @Test
  void shouldDoNothingBecauseExistProductCached() {
    var command =
        AddFavoriteCommand.builder()
            .customerId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();
    Cache cache = mock(Cache.class);
    given(cacheManager.getCache(CacheConfiguration.PRODUCT_CACHE_NAME)).willReturn(cache);
    given(cache.get(command.getProductId())).willReturn(mock(ValueWrapper.class));

    var throwableAssert = assertThatCode(() -> validation.valid(command));

    throwableAssert.doesNotThrowAnyException();
    then(productEaiClient).should(never()).getProduct(command.getProductId());
    then(cache).should(never()).put(command.getProductId(), true);
  }

  @Test
  void shouldThrowErrorBecauseNoExistProduct() {
    var command =
        AddFavoriteCommand.builder()
            .customerId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();
    Cache cache = mock(Cache.class);
    given(cacheManager.getCache(CacheConfiguration.PRODUCT_CACHE_NAME)).willReturn(cache);
    given(cache.get(command.getProductId())).willReturn(null);
    given(productEaiClient.getProduct(command.getProductId()))
        .willReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

    var throwableAssert = assertThatThrownBy(() -> validation.valid(command));

    throwableAssert
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("Não foi encontrado o produto.");
    then(cache).should(never()).put(command.getProductId(), true);
  }
}
