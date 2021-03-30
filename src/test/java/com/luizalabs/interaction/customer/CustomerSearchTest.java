package com.luizalabs.interaction.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.luizalabs.entity.Customer;
import com.luizalabs.exception.ResourceNotFoundException;
import com.luizalabs.interaction.customer.response.CustomerResponse;
import com.luizalabs.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class CustomerSearchTest {

  @Mock private CustomerRepository repository;
  @InjectMocks private CustomerSearch interaction;

  @Test
  void shouldGetCustomer() {
    var pageRequest = PageRequest.of(1, 10);
    var customerId = UUID.randomUUID();
    var customer = mock(Customer.class);
    var response = mock(CustomerResponse.class);
    given(customer.toResponse()).willReturn(response);

    given(repository.findById(customerId)).willReturn(Optional.of(customer));

    var result = interaction.get(customerId);

    assertThat(result).isSameAs(response);
  }

  @Test
  void shouldDoNotGetCustomerWhenNotExist() {
    var customerId = UUID.randomUUID();

    given(repository.findById(customerId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> interaction.get(customerId))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("Não foi encontrado o cliente.");
  }

  @Test
  void shouldListCustomersPageable() {
    var pageRequest = PageRequest.of(1, 10);
    var customer = mock(Customer.class);
    var response = mock(CustomerResponse.class);
    given(customer.toResponse()).willReturn(response);
    PageImpl pageResult = new PageImpl(List.of(customer));
    given(repository.findAll(pageRequest)).willReturn(pageResult);

    var page = interaction.list(pageRequest);

    assertThat(page.get()).containsOnly(response);
  }
}
