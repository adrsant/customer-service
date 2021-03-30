package com.luizalabs.interaction.customer;

import com.luizalabs.entity.Customer;
import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.interaction.customer.response.CustomerResponse;
import com.luizalabs.repository.CustomerRepository;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class CustomerSearch {

  private final CustomerRepository repository;

  public CustomerResponse get(@Valid @NotNull UUID id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado o cliente."))
        .toResponse();
  }

  public Page<CustomerResponse> list(Pageable pageable) {
    return repository.findAll(pageable).map(Customer::toResponse);
  }
}
