package com.luizalabs.interaction.favority;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.interaction.favority.command.AddFavoriteCommand;
import com.luizalabs.repository.CustomerRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExistCustomerListValidationTest {

  @Mock private CustomerRepository repository;
  @InjectMocks private ExistCustomerListValidation validation;

  @Test
  void shouldDoNothingBecauseExistCustomer() {
    var command =
        AddFavoriteCommand.builder()
            .customerId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();

    given(repository.existsById(command.getCustomerId())).willReturn(true);

    var throwableAssert = assertThatCode(() -> validation.valid(command));

    throwableAssert.doesNotThrowAnyException();
  }

  @Test
  void shouldThrowErrorBecauseNoExistCustomer() {
    var command =
        AddFavoriteCommand.builder()
            .customerId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();
    given(repository.existsById(command.getCustomerId())).willReturn(false);

    var throwableAssert = assertThatThrownBy(() -> validation.valid(command));

    throwableAssert
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("The client not exists");
  }
}
