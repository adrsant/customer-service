package com.luizalabs.interaction.customer;

import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.interaction.ValidationBeforeInteraction;
import com.luizalabs.interaction.customer.command.RemoveCustomerCommand;
import com.luizalabs.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ExistCustomerValidation implements ValidationBeforeInteraction<RemoveCustomerCommand> {

  private final CustomerRepository repository;

  @Override
  public void valid(RemoveCustomerCommand command) {
    boolean exists = repository.existsById(command.getCustomerId());

    if (!exists) {
      throw new ResourceNotFoundException("The customer not exists.");
    }
  }
}
