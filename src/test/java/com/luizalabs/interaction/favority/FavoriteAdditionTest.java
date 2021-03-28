package com.luizalabs.interaction.favority;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;

import com.luizalabs.entity.FavoriteProduct;
import com.luizalabs.exception.ResourceDuplicatedException;
import com.luizalabs.interaction.favority.command.AddFavoriteCommand;
import com.luizalabs.repository.FavoriteProductRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteAdditionTest {

  @Mock private FavoriteProductRepository repository;
  @Mock private ExistCustomerListValidation validation;
  private FavoriteAddition interaction;

  @BeforeEach
  void setUp() {
    interaction = new FavoriteAddition(List.of(validation), repository);
  }

  @Test
  void shouldAddProductInFavoriteList() {
    var command =
        AddFavoriteCommand.builder()
            .customerId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();
    var entityCreated =
        FavoriteProduct.builder()
            .customerId(command.getCustomerId())
            .productId(command.getProductId())
            .build();
    doNothing().when(validation).valid(command);

    interaction.add(command);

    then(repository).should().save(entityCreated);
  }

  @Test
  void shouldDoNotAddProductWhenHasValidationError() {
    var command =
        AddFavoriteCommand.builder()
            .customerId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();

    doThrow(ResourceDuplicatedException.class).when(validation).valid(command);

    assertThatThrownBy(() -> interaction.add(command))
        .isInstanceOf(ResourceDuplicatedException.class);
    then(repository).should(never()).save(any());
  }
}
