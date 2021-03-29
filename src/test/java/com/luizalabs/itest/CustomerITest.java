package com.luizalabs.itest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizalabs.interaction.customer.command.CreateCustomerCommand;
import com.luizalabs.interaction.customer.command.RemoveCustomerCommand;
import com.luizalabs.interaction.customer.command.UpdateClientCommand;
import com.luizalabs.repository.CustomerRepository;
import com.luizalabs.test.ContextTestMockDatabase;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
@WithMockUser(value = "mock", authorities = "WRITE_PRIVILEGE")
class CustomerITest extends ContextTestMockDatabase {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String CONTEXT_ROOT = "/api/customer/";
  @Autowired private MockMvc mockMvc;
  @Autowired private CustomerRepository repository;

  @Test
  @Sql(scripts = "classpath:test-data/clean-database.sql")
  public void shouldCreateCustomer() throws Exception {
    var command = CreateCustomerCommand.builder().mail("test100@mail.com").name("name").build();

    var json = OBJECT_MAPPER.writeValueAsString(command);

    mockMvc
        .perform(post(CONTEXT_ROOT).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.mail").value("test100@mail.com"))
        .andExpect(jsonPath("$.name").value("name"))
        .andReturn();

    assertThat(repository.existsByMail(command.getMail())).isTrue();
  }

  @Test
  @Sql(scripts = "classpath:test-data/create-customers.sql")
  public void shouldDoNotCreateCustomerBecauseMailDuplicated() throws Exception {
    var command = CreateCustomerCommand.builder().mail("test@mail.com").name("Teste name").build();

    var json = OBJECT_MAPPER.writeValueAsString(command);

    mockMvc
        .perform(post(CONTEXT_ROOT).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.errors.[0]").value("The email has already been used by another customer"))
        .andReturn();
  }

  @Test
  @Sql(scripts = "classpath:test-data/create-customers.sql")
  public void shouldUpdateCustomer() throws Exception {
    String uuid = "6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11";
    var command =
        UpdateClientCommand.builder()
            .id(UUID.fromString(uuid))
            .mail(uuid + "@mail.com")
            .name("name-" + uuid)
            .build();
    var json = OBJECT_MAPPER.writeValueAsString(command);

    mockMvc
        .perform(put(CONTEXT_ROOT).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(uuid))
        .andExpect(jsonPath("$.mail").value(command.getMail()))
        .andExpect(jsonPath("$.name").value(command.getName()))
        .andReturn();

    assertThat(repository.existsByMail(command.getMail())).isTrue();
  }

  @Test
  @Sql(scripts = "classpath:test-data/create-customers.sql")
  public void shouldDoNotUpdateCustomerBecauseMailDuplicated() throws Exception {
    String uuid = "6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11";
    var command =
        UpdateClientCommand.builder()
            .id(UUID.fromString(uuid))
            .mail("test_2@mail.com")
            .name("name-" + uuid)
            .build();
    var json = OBJECT_MAPPER.writeValueAsString(command);

    mockMvc
        .perform(put(CONTEXT_ROOT).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(
            jsonPath("$.errors.[0]").value("The email has already been used by another customer"))
        .andReturn();

    assertThat(repository.existsByMail(command.getMail())).isTrue();
  }

  @Test
  @Sql(scripts = "classpath:test-data/clean-database.sql")
  public void shouldDoNotUpdateCustomerBecauseConsumerNotExists() throws Exception {
    String uuid = "6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11";
    var command =
        UpdateClientCommand.builder()
            .id(UUID.fromString(uuid))
            .mail("test_2@mail.com")
            .name("name-" + uuid)
            .build();
    var json = OBJECT_MAPPER.writeValueAsString(command);

    mockMvc
        .perform(put(CONTEXT_ROOT).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(
            jsonPath("$.errors.[0]")
                .value("Customer not found with id 6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11"))
        .andReturn();
  }

  @Test
  @Sql(scripts = "classpath:test-data/create-customers.sql")
  public void shouldRemovalCustomer() throws Exception {
    String uuid = "6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11";
    var command = RemoveCustomerCommand.builder().customerId(UUID.fromString(uuid)).build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(CONTEXT_ROOT + "{customerId}/", command.getCustomerId()))
        .andExpect(status().isNoContent())
        .andReturn();

    assertThat(repository.existsById(command.getCustomerId())).isFalse();
  }

  @Test
  @Sql(scripts = "classpath:test-data/clean-database.sql")
  public void shouldDoNotRemovalCustomerBecauseConsumerNotExists() throws Exception {
    String uuid = "6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11";
    var command = RemoveCustomerCommand.builder().customerId(UUID.fromString(uuid)).build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(CONTEXT_ROOT + "{customerId}/", command.getCustomerId()))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors.[0]").value("The customer not exists."))
        .andReturn();
  }

  @Test
  @Sql(scripts = "classpath:test-data/create-customers.sql")
  public void shouldGetCustomer() throws Exception {
    String uuid = "6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11";

    mockMvc
        .perform(get(CONTEXT_ROOT + "{customerId}/", uuid))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(uuid))
        .andExpect(jsonPath("$.mail").value("test@mail.com"))
        .andExpect(jsonPath("$.name").value("Test"))
        .andReturn();
  }

  @Test
  @Sql(scripts = "classpath:test-data/clean-database.sql")
  public void shouldDoNotGetCustomerBecauseConsumerNotExists() throws Exception {
    String uuid = "6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11";

    mockMvc
        .perform(get(CONTEXT_ROOT + "{customerId}/", uuid))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(
            jsonPath("$.errors.[0]")
                .value("Customer not found with id 6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11"))
        .andReturn();
  }

  @Test
  @Sql(scripts = "classpath:test-data/create-customers.sql")
  public void shouldListCustomers() throws Exception {
    mockMvc
        .perform(get(CONTEXT_ROOT))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content.*", hasSize(2)))
        .andReturn();
  }
}
