package com.luizalabs.interaction.favority;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;

import com.luizalabs.entity.pk.FavoriteProductPk;
import com.luizalabs.exception.ResourceDuplicatedException;
import com.luizalabs.interaction.favority.command.RemoveFavoriteCommand;
import com.luizalabs.repository.FavoriteProductRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteRemovalTest {

  @Mock private FavoriteProductRepository repository;
  @Mock private ExistFavoriteProductValidation validation;
  private FavoriteRemoval interaction;

  @BeforeEach
  void setUp() {
    interaction = new FavoriteRemoval(List.of(validation), repository);
  }

  @Test
  void shouldRemoveProductFromFavoriteList() {
    var command =
        RemoveFavoriteCommand.builder()
            .customerId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();
    var pk =
        FavoriteProductPk.builder()
            .customerId(command.getCustomerId())
            .productId(command.getProductId())
            .build();
    doNothing().when(validation).valid(command);

    interaction.remove(command);

    then(repository).should().deleteById(pk);
  }

  @Test
  void shouldDoNotAddProductWhenHasValidationError() {
    var command =
        RemoveFavoriteCommand.builder()
            .customerId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();
    var pk =
        FavoriteProductPk.builder()
            .customerId(command.getCustomerId())
            .productId(command.getProductId())
            .build();

    doThrow(ResourceDuplicatedException.class).when(validation).valid(command);

    assertThatThrownBy(() -> interaction.remove(command))
        .isInstanceOf(ResourceDuplicatedException.class);
    then(repository).should(never()).deleteById(any());
  }
}
