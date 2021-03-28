package com.luizalabs.interaction.customer;

import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.interaction.ValidationBeforeInteraction;
import com.luizalabs.interaction.customer.command.UpdateClientCommand;
import com.luizalabs.interaction.customer.response.CustomerResponse;
import com.luizalabs.repository.CustomerRepository;
import java.util.Collection;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class CustomerUpdate {

  private final Collection<ValidationBeforeInteraction<UpdateClientCommand>> validations;
  private final CustomerRepository repository;

  @Transactional
  public CustomerResponse update(@Valid UpdateClientCommand command) {
    validations.forEach(validation -> validation.valid(command));

    var customer =
        repository
            .findById(command.getId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException("Customer not found with id " + command.getId()));

    customer.updateInfo(command.getName(), command.getMail());

    repository.save(customer);

    log.info("customer has been updated id {}", customer.getId());

    return customer.toResponse();
  }
}
