package com.luizalabs.interaction.customer.command;

import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UpdateClientCommand {

  @NotNull private UUID id;
  @NotNull @Email private String mail;
  @NotNull @NotBlank private String name;
}
