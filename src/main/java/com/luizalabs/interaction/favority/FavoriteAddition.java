package com.luizalabs.interaction.favority;

import com.luizalabs.entity.FavoriteProduct;
import com.luizalabs.interaction.ValidationBeforeInteraction;
import com.luizalabs.interaction.favority.command.AddFavoriteCommand;
import com.luizalabs.repository.FavoriteProductRepository;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FavoriteAddition {

  private final Collection<ValidationBeforeInteraction<AddFavoriteCommand>> validations;
  private final FavoriteProductRepository repository;

  public void add(AddFavoriteCommand command) {
    validations.forEach(validation -> validation.valid(command));

    var favoriteProduct =
        FavoriteProduct.builder()
            .customerId(command.getCustomerId())
            .productId(command.getProductId())
            .build();

    repository.save(favoriteProduct);

    log.info(
        "product id {} has been added to lis of customer id {}",
        command.getProductId(),
        command.getCustomerId());
  }
}
