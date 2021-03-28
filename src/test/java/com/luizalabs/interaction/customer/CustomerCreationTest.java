package com.luizalabs.interaction.customer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;

import com.luizalabs.entity.Customer;
import com.luizalabs.exception.ResourceDuplicatedException;
import com.luizalabs.interaction.customer.command.CreateCustomerCommand;
import com.luizalabs.repository.CustomerRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerCreationTest {

  @Mock private CustomerRepository repository;
  @Mock private ExistMailValidation validation;
  private CustomerCreation interaction;

  @BeforeEach
  void setUp() {
    interaction = new CustomerCreation(List.of(validation), repository);
  }

  @Test
  void shouldCreateCustomer() {
    var command = CreateCustomerCommand.builder().name("name").mail("mail").build();
    var entityCreated = Customer.builder().mail(command.getMail()).name(command.getName()).build();

    doNothing().when(validation).valid(command);

    interaction.create(command);

    then(repository).should().save(entityCreated);
  }

  @Test
  void shouldDoNotCreateCustomerWhenHasValidationError() {
    var command = CreateCustomerCommand.builder().name("name").mail("mail").build();

    doThrow(ResourceDuplicatedException.class).when(validation).valid(command);

    assertThatThrownBy(() -> interaction.create(command))
        .isInstanceOf(ResourceDuplicatedException.class);
    then(repository).should(never()).save(any());
  }
}
