package com.luizalabs.interaction.customer.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerResponse {

  private final UUID id;
  private final String mail;
  private final String name;
}
