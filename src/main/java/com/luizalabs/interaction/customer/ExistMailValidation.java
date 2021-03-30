package com.luizalabs.interaction.customer;

import com.luizalabs.exception.ResourceDuplicatedException;
import com.luizalabs.interaction.ValidationBeforeInteraction;
import com.luizalabs.interaction.customer.command.CreateCustomerCommand;
import com.luizalabs.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ExistMailValidation implements ValidationBeforeInteraction<CreateCustomerCommand> {

  private final CustomerRepository repository;

  @Override
  public void valid(CreateCustomerCommand command) {
    boolean exists = repository.existsByMail(command.getMail());

    if (exists) {
      throw new ResourceDuplicatedException("O email já está sendo utilizado.");
    }
  }
}
