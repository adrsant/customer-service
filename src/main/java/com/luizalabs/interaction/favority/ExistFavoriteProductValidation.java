package com.luizalabs.interaction.favority;

import com.luizalabs.entity.pk.FavoriteProductPk;
import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.interaction.ValidationBeforeInteraction;
import com.luizalabs.interaction.favority.command.RemoveFavoriteCommand;
import com.luizalabs.repository.FavoriteProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ExistFavoriteProductValidation implements ValidationBeforeInteraction<RemoveFavoriteCommand> {

  private final FavoriteProductRepository repository;

  @Override
  public void valid(RemoveFavoriteCommand command) {
    var pk =
        FavoriteProductPk.builder()
            .customerId(command.getCustomerId())
            .productId(command.getProductId())
            .build();

    boolean exists = repository.existsById(pk);

    if (!exists) {
      throw new ResourceNotFoundException("Não foi encontrado o produto favorito.");
    }
  }
}
