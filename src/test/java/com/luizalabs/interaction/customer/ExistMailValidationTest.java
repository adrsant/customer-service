package com.luizalabs.interaction.customer;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.luizalabs.exception.ResourceDuplicatedException;
import com.luizalabs.interaction.customer.command.CreateCustomerCommand;
import com.luizalabs.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExistMailValidationTest {

  @Mock private CustomerRepository repository;
  @InjectMocks private ExistMailValidation validation;

  @Test
  void shouldDoNothingBecauseNotExistsEmail() {
    var command = CreateCustomerCommand.builder().mail("mail").name("name").build();

    given(repository.existsByMail(command.getMail())).willReturn(false);

    var throwableAssert = assertThatCode(() -> validation.valid(command));

    throwableAssert.doesNotThrowAnyException();
  }

  @Test
  void shouldThrowErrorBecauseExistsEmail() {
    var command = CreateCustomerCommand.builder().mail("mail").name("name").build();

    given(repository.existsByMail(command.getMail())).willReturn(true);

    var throwableAssert = assertThatThrownBy(() -> validation.valid(command));

    throwableAssert
        .isInstanceOf(ResourceDuplicatedException.class)
        .hasMessage("The email has already been used by another customer");
  }
}
