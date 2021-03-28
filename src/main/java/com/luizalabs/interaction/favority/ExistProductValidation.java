package com.luizalabs.interaction.favority;

import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.infrastructure.eai.product.ProductEaiClient;
import com.luizalabs.interaction.ValidationBeforeInteraction;
import com.luizalabs.interaction.favority.command.AddFavoriteCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ExistProductValidation implements ValidationBeforeInteraction<AddFavoriteCommand> {

  private final ProductEaiClient productEaiClient;

  @Override
  public void valid(AddFavoriteCommand command) {
    var responseEntity = productEaiClient.getProduct(command.getProductId());

    if (HttpStatus.OK != responseEntity.getStatusCode()) {
      throw new ResourceNotFoundException("The product not exists");
    }
  }
}
