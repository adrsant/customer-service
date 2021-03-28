package com.luizalabs.interaction.customer;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;

import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.interaction.customer.command.RemoveCustomerCommand;
import com.luizalabs.repository.CustomerRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExistCustomerValidationTest {

  @Mock private CustomerRepository repository;
  @InjectMocks private ExistCustomerValidation validation;

  @Test
  void shouldDoNothingBecauseExistsProduct() {
    var command = RemoveCustomerCommand.builder().customerId(UUID.randomUUID()).build();

    given(repository.existsById(command.getCustomerId())).willReturn(true);

    var throwableAssert = assertThatCode(() -> validation.valid(command));

    throwableAssert.doesNotThrowAnyException();
  }

  @Test
  void shouldThrowErrorBecauseNotExistsProduct() {
    var command = RemoveCustomerCommand.builder().customerId(UUID.randomUUID()).build();

    given(repository.existsById(command.getCustomerId())).willReturn(false);

    var throwableAssert = assertThatCode(() -> validation.valid(command));

    throwableAssert
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("The customer not exists.");
  }
}
