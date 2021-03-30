package com.luizalabs.interaction.favority;

import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.infrastructure.configuration.CacheConfiguration;
import com.luizalabs.infrastructure.eai.product.ProductEaiClient;
import com.luizalabs.interaction.ValidationBeforeInteraction;
import com.luizalabs.interaction.favority.command.AddFavoriteCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ExistProductValidation implements ValidationBeforeInteraction<AddFavoriteCommand> {

  private final ProductEaiClient productEaiClient;
  private final CacheManager cacheManager;

  @Override
  public void valid(AddFavoriteCommand command) {
    var cache = cacheManager.getCache(CacheConfiguration.PRODUCT_CACHE_NAME);
    var productCached = cache.get(command.getProductId()) != null;

    if (productCached) {
      return;
    }

    var responseEntity = productEaiClient.getProduct(command.getProductId());

    if (HttpStatus.OK != responseEntity.getStatusCode()) {
      throw new ResourceNotFoundException("Não foi encontrado o produto.");
    }

    cache.put(command.getProductId(), true);
  }
}
