package com.luizalabs.interaction.customer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import com.luizalabs.entity.Customer;
import com.luizalabs.exception.ResourceDuplicatedException;
import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.interaction.customer.command.UpdateClientCommand;
import com.luizalabs.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerUpdateTest {

  @Mock private CustomerRepository repository;
  @Mock private CanUpdateMailValidation validation;
  private CustomerUpdate interaction;

  @BeforeEach
  void setUp() {
    interaction = new CustomerUpdate(List.of(validation), repository);
  }

  @Test
  void shouldUpdateCustomer() {
    var command =
        UpdateClientCommand.builder().id(UUID.randomUUID()).name("name").mail("mail").build();
    var entityCreated = mock(Customer.class);
    given(repository.findById(command.getId())).willReturn(Optional.of(entityCreated));
    doNothing().when(validation).valid(command);

    interaction.update(command);

    then(entityCreated).should().updateInfo(command.getName(), command.getMail());
    then(repository).should().save(entityCreated);
  }

  @Test
  void shouldDoNotUpdateCustomerWhenNotFound() {
    var command =
        UpdateClientCommand.builder().id(UUID.randomUUID()).name("name").mail("mail").build();
    var entityCreated = mock(Customer.class);
    given(repository.findById(command.getId())).willReturn(Optional.empty());
    doNothing().when(validation).valid(command);

    assertThatThrownBy(() -> interaction.update(command))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("Customer not found with id " + command.getId());

    then(entityCreated).should(never()).updateInfo(command.getName(), command.getMail());
    then(repository).should(never()).save(entityCreated);
  }

  @Test
  void shouldDoNotUpdateCustomerWhenHasValidationError() {
    var command =
        UpdateClientCommand.builder().id(UUID.randomUUID()).name("name").mail("mail").build();

    doThrow(ResourceDuplicatedException.class).when(validation).valid(command);

    assertThatThrownBy(() -> interaction.update(command))
        .isInstanceOf(ResourceDuplicatedException.class);
    then(repository).should(never()).save(any());
  }
}
