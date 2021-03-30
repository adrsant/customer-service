package com.luizalabs.interaction.customer;

import com.luizalabs.exception.ResourceDuplicatedException;
import com.luizalabs.interaction.ValidationBeforeInteraction;
import com.luizalabs.interaction.customer.command.UpdateClientCommand;
import com.luizalabs.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CanUpdateMailValidation implements ValidationBeforeInteraction<UpdateClientCommand> {

  private final CustomerRepository repository;

  @Override
  public void valid(UpdateClientCommand command) {
    boolean exists = repository.existsByMailAndIdNot(command.getMail(), command.getId());

    if (exists) {
      throw new ResourceDuplicatedException("O email já está sendo utilizado.");
    }
  }
}
