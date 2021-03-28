package com.luizalabs.interaction.favority;

import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.interaction.ValidationBeforeInteraction;
import com.luizalabs.interaction.favority.command.AddFavoriteCommand;
import com.luizalabs.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ExistCustomerListValidation implements ValidationBeforeInteraction<AddFavoriteCommand> {

  private final CustomerRepository repository;

  @Override
  public void valid(AddFavoriteCommand command) {
    boolean exists = repository.existsById(command.getCustomerId());

    if (!exists) {
      throw new ResourceNotFoundException("The client not exists");
    }
  }
}
