package com.luizalabs.interaction.favority;

import com.luizalabs.entity.pk.FavoriteProductPk;
import com.luizalabs.exception.ResourceDuplicatedException;
import com.luizalabs.interaction.ValidationBeforeInteraction;
import com.luizalabs.interaction.favority.command.AddFavoriteCommand;
import com.luizalabs.repository.FavoriteProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class NotExistFavoriteProductValidation implements ValidationBeforeInteraction<AddFavoriteCommand> {

  private final FavoriteProductRepository repository;

  @Override
  public void valid(AddFavoriteCommand command) {
    var pk =
        FavoriteProductPk.builder()
            .customerId(command.getCustomerId())
            .productId(command.getProductId())
            .build();

    boolean exists = repository.existsById(pk);

    if (exists) {
      throw new ResourceDuplicatedException("The product has been in favorite list.");
    }
  }
}
