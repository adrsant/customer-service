package com.luizalabs.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class CustomerTest {

  @Test
  void shouldUpdateInfo() {
    var newName = "name2";
    var newMail = "mail2";
    var customer = Customer.builder().id(UUID.randomUUID()).mail("mail").name("name").build();

    customer.updateInfo(newName, newMail);

    assertThat(customer).extracting("name", "mail").containsExactly(newName, newMail);
  }
}
