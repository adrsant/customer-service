package com.luizalabs.interaction.favority;

import com.luizalabs.entity.pk.FavoriteProductPk;
import com.luizalabs.interaction.ValidationBeforeInteraction;
import com.luizalabs.interaction.favority.command.RemoveFavoriteCommand;
import com.luizalabs.repository.FavoriteProductRepository;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class FavoriteRemoval {

  private final Collection<ValidationBeforeInteraction<RemoveFavoriteCommand>> validations;
  private final FavoriteProductRepository repository;

  public void remove(RemoveFavoriteCommand command) {
    validations.forEach(validation -> validation.valid(command));

    var pk =
        FavoriteProductPk.builder()
            .customerId(command.getCustomerId())
            .productId(command.getProductId())
            .build();

    repository.deleteById(pk);

    log.info(
        "product id {} has been removed from lis of customer id {}",
        command.getProductId(),
        command.getCustomerId());
  }
}
