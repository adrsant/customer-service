package com.luizalabs.interaction.favority;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.infrastructure.eai.product.ProductEaiClient;
import com.luizalabs.interaction.favority.command.AddFavoriteCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ExistProductValidationTest {

  @Mock private ProductEaiClient productEaiClient;
  @InjectMocks private ExistProductValidation validation;

  @Test
  void shouldDoNothingBecauseExistProduct() {
    var command =
        AddFavoriteCommand.builder()
            .customerId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();

    given(productEaiClient.getProduct(command.getProductId()))
        .willReturn(ResponseEntity.ok().build());

    var throwableAssert = assertThatCode(() -> validation.valid(command));

    throwableAssert.doesNotThrowAnyException();
  }

  @Test
  void shouldThrowErrorBecauseNoExistProduct() {
    var command =
        AddFavoriteCommand.builder()
            .customerId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();

    given(productEaiClient.getProduct(command.getProductId()))
        .willReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

    var throwableAssert = assertThatThrownBy(() -> validation.valid(command));

    throwableAssert
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("The product not exists");
  }
}
