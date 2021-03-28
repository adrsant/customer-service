package com.luizalabs.interaction.favority.command;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class RemoveFavoriteCommand {

  @NotNull private final UUID customerId;
  @NotNull private final UUID productId;
}
