package com.luizalabs.interaction.customer;

import com.luizalabs.interaction.ValidationBeforeInteraction;
import com.luizalabs.interaction.customer.command.RemoveCustomerCommand;
import com.luizalabs.repository.CustomerRepository;
import com.luizalabs.repository.FavoriteProductRepository;
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
public class CustomerRemoval {

  private final Collection<ValidationBeforeInteraction<RemoveCustomerCommand>> validations;
  private final CustomerRepository repository;
  private final FavoriteProductRepository favoriteProductRepository;

  @Transactional
  public void delete(@Valid RemoveCustomerCommand command) {
    validations.forEach(validation -> validation.valid(command));

    repository.deleteById(command.getCustomerId());

    log.info("customer has been removed id {}", command.getCustomerId());
  }
}
