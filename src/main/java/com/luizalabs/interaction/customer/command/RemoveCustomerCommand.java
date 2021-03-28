package com.luizalabs.interaction.customer.command;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class RemoveCustomerCommand {

  @NotNull private final UUID customerId;
}
