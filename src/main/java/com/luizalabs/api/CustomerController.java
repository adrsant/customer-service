package com.luizalabs.api;

import com.luizalabs.interaction.customer.CustomerCreation;
import com.luizalabs.interaction.customer.CustomerRemoval;
import com.luizalabs.interaction.customer.CustomerSearch;
import com.luizalabs.interaction.customer.CustomerUpdate;
import com.luizalabs.interaction.customer.command.CreateCustomerCommand;
import com.luizalabs.interaction.customer.command.RemoveCustomerCommand;
import com.luizalabs.interaction.customer.command.UpdateClientCommand;
import com.luizalabs.interaction.customer.response.CustomerResponse;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/")
public class CustomerController {

  private final CustomerCreation customerCreation;
  private final CustomerRemoval customerRemoval;
  private final CustomerSearch customerSearch;
  private final CustomerUpdate customerUpdate;

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CustomerResponse create(@Valid @RequestBody CreateCustomerCommand request) {
    return customerCreation.create(request);
  }

  @DeleteMapping("{customerId}/")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID customerId) {
    var command = RemoveCustomerCommand.builder().customerId(customerId).build();
    customerRemoval.delete(command);
  }

  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CustomerResponse update(@Valid @RequestBody UpdateClientCommand command) {
    return customerUpdate.update(command);
  }

  @GetMapping(path = "{customerId}/", produces = MediaType.APPLICATION_JSON_VALUE)
  public CustomerResponse find(@PathVariable UUID customerId) {
    return customerSearch.get(customerId);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<CustomerResponse> listAll(@PageableDefault Pageable pageable) {
    return customerSearch.list(pageable);
  }
}
