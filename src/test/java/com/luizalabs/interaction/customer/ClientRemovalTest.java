package com.luizalabs.interaction.customer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;

import com.luizalabs.exception.ResourceDuplicatedException;
import com.luizalabs.interaction.customer.command.RemoveCustomerCommand;
import com.luizalabs.repository.CustomerRepository;
import com.luizalabs.repository.FavoriteProductRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientRemovalTest {

  @Mock private CustomerRepository repository;
  @Mock private ExistCustomerValidation validation;
  @Mock private FavoriteProductRepository favoriteProductRepository;
  private CustomerRemoval interaction;

  @BeforeEach
  void setUp() {
    interaction = new CustomerRemoval(List.of(validation), repository, favoriteProductRepository);
  }

  @Test
  void shouldRemoveCustomer() {
    var command = RemoveCustomerCommand.builder().customerId(UUID.randomUUID()).build();
    doNothing().when(validation).valid(command);

    interaction.delete(command);

    then(repository).should().deleteById(command.getCustomerId());
  }

  @Test
  void shouldDoNotUpdateCustomerWhenHasValidationError() {
    var command = RemoveCustomerCommand.builder().customerId(UUID.randomUUID()).build();

    doThrow(ResourceDuplicatedException.class).when(validation).valid(command);

    assertThatThrownBy(() -> interaction.delete(command))
        .isInstanceOf(ResourceDuplicatedException.class);
    then(repository).should(never()).deleteById(command.getCustomerId());
  }
}
