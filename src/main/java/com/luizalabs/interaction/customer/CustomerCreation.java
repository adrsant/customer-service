package com.luizalabs.interaction.customer;

import com.luizalabs.entity.Customer;
import com.luizalabs.interaction.ValidationBeforeInteraction;
import com.luizalabs.interaction.customer.command.CreateCustomerCommand;
import com.luizalabs.interaction.customer.response.CustomerResponse;
import com.luizalabs.repository.CustomerRepository;
import java.util.Collection;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class CustomerCreation {

  private final Collection<ValidationBeforeInteraction<CreateCustomerCommand>> validations;
  private final CustomerRepository repository;

  public CustomerResponse create(@Valid CreateCustomerCommand command) {

    validations.forEach(validation -> validation.valid(command));
    var customer = Customer.builder().mail(command.getMail()).name(command.getName()).build();
    repository.save(customer);

    log.info("customer has been created id {}", customer.getId());

    return customer.toResponse();
  }
}
