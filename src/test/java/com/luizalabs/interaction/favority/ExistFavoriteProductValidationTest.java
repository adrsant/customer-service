package com.luizalabs.interaction.favority;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.luizalabs.entity.pk.FavoriteProductPk;
import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.interaction.favority.command.RemoveFavoriteCommand;
import com.luizalabs.repository.FavoriteProductRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExistFavoriteProductValidationTest {

  @Mock private FavoriteProductRepository repository;
  @InjectMocks private ExistFavoriteProductValidation validation;

  @Test
  void shouldDoNothingBecauseExistFavoriteProduct() {
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

    given(repository.existsById(pk)).willReturn(true);

    var throwableAssert = assertThatCode(() -> validation.valid(command));

    throwableAssert.doesNotThrowAnyException();
  }

  @Test
  void shouldThrowErrorBecauseNoExistFavoriteProduct() {
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

    given(repository.existsById(pk)).willReturn(false);

    var throwableAssert = assertThatThrownBy(() -> validation.valid(command));

    throwableAssert
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("Não foi encontrado o produto favorito.");
  }
}
