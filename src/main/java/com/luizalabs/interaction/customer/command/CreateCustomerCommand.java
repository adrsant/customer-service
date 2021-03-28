package com.luizalabs.interaction.customer.command;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
public class CreateCustomerCommand {
  @NotBlank @Email private String mail;
  @NotBlank private String name;
}
