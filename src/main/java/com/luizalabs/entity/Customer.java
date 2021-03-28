package com.luizalabs.entity;

import com.luizalabs.interaction.customer.response.CustomerResponse;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "mail")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotNull @Email private String mail;
  @NotNull @NotBlank private String name;

  public void updateInfo(String name, String mail) {
    this.name = name;
    this.mail = mail;
  }

  public CustomerResponse toResponse() {
    return CustomerResponse.builder().id(id).mail(mail).name(name).build();
  }
}
