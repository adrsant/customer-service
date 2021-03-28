package com.luizalabs.interaction.favority;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.luizalabs.entity.pk.FavoriteProductPk;
import com.luizalabs.exception.ResourceDuplicatedException;
import com.luizalabs.interaction.favority.command.AddFavoriteCommand;
import com.luizalabs.repository.FavoriteProductRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotExistFavoriteProductValidationTest {

  @Mock private FavoriteProductRepository repository;
  @InjectMocks private NotExistFavoriteProductValidation validation;

  @Test
  void shouldDoNothingBecauseNotExistFavoriteProduct() {
    var command =
        AddFavoriteCommand.builder()
            .customerId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();
    var pk =
        FavoriteProductPk.builder()
            .customerId(command.getCustomerId())
            .productId(command.getProductId())
            .build();

    given(repository.existsById(pk)).willReturn(false);

    var throwableAssert = assertThatCode(() -> validation.valid(command));

    throwableAssert.doesNotThrowAnyException();
  }

  @Test
  void shouldThrowErrorBecauseExistFavoriteProduct() {
    var command =
        AddFavoriteCommand.builder()
            .customerId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();
    var pk =
        FavoriteProductPk.builder()
            .customerId(command.getCustomerId())
            .productId(command.getProductId())
            .build();

    given(repository.existsById(pk)).willReturn(true);

    var throwableAssert = assertThatThrownBy(() -> validation.valid(command));

    throwableAssert
        .isInstanceOf(ResourceDuplicatedException.class)
        .hasMessage("The product has been in favorite list.");
  }
}
