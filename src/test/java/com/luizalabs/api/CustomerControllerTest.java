package com.luizalabs.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizalabs.api.handler.CustomGlobalExceptionHandler;
import com.luizalabs.interaction.customer.CustomerCreation;
import com.luizalabs.interaction.customer.CustomerRemoval;
import com.luizalabs.interaction.customer.CustomerSearch;
import com.luizalabs.interaction.customer.CustomerUpdate;
import com.luizalabs.interaction.customer.command.CreateCustomerCommand;
import com.luizalabs.interaction.customer.command.RemoveCustomerCommand;
import com.luizalabs.interaction.customer.command.UpdateClientCommand;
import com.luizalabs.interaction.customer.response.CustomerResponse;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String CONTEXT_ROOT = "/api/customer/";
  @Mock private CustomerCreation customerCreation;
  @Mock private CustomerRemoval customerRemoval;
  @Mock private CustomerSearch customerSearch;
  @Mock private CustomerUpdate customerUpdate;
  private MockMvc mockMvc;
  @InjectMocks private CustomerController controller;

  @BeforeEach
  public void setup() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new CustomGlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
  }

  @Test
  public void shouldCreateCustomer() throws Exception {
    var command = CreateCustomerCommand.builder().mail("test@mail.com").name("name").build();
    var json = OBJECT_MAPPER.writeValueAsString(command);
    var response =
        CustomerResponse.builder()
            .id(UUID.randomUUID())
            .mail(command.getMail())
            .name(command.getMail())
            .build();
    given(customerCreation.create(command)).willReturn(response);

    mockMvc
        .perform(post(CONTEXT_ROOT).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(response.getId().toString()))
        .andExpect(jsonPath("$.mail").value(response.getMail()))
        .andExpect(jsonPath("$.name").value(response.getName()))
        .andReturn();
  }

  @Test
  public void shouldUpdateCustomer() throws Exception {
    var command =
        UpdateClientCommand.builder()
            .id(UUID.randomUUID())
            .mail("test@mail.com")
            .name("name")
            .build();
    var json = OBJECT_MAPPER.writeValueAsString(command);
    var response =
        CustomerResponse.builder()
            .id(command.getId())
            .mail(command.getMail())
            .name(command.getMail())
            .build();
    given(customerUpdate.update(command)).willReturn(response);

    mockMvc
        .perform(put(CONTEXT_ROOT).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(response.getId().toString()))
        .andExpect(jsonPath("$.mail").value(response.getMail()))
        .andExpect(jsonPath("$.name").value(response.getName()))
        .andReturn();
  }

  @Test
  public void shouldRemovalCustomer() throws Exception {
    var command = RemoveCustomerCommand.builder().customerId(UUID.randomUUID()).build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(CONTEXT_ROOT + "{customerId}/", command.getCustomerId()))
        .andExpect(status().isNoContent())
        .andReturn();

    then(customerRemoval).should().delete(command);
  }

  @Test
  public void shouldGetCustomer() throws Exception {
    var response =
        CustomerResponse.builder().id(UUID.randomUUID()).mail("test@mail.com").name("name").build();

    given(customerSearch.get(response.getId())).willReturn(response);

    mockMvc
        .perform(get(CONTEXT_ROOT + "{customerId}/", response.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(response.getId().toString()))
        .andExpect(jsonPath("$.mail").value(response.getMail()))
        .andExpect(jsonPath("$.name").value(response.getName()))
        .andReturn();
  }

  @Test
  public void shouldListCustomers() throws Exception {
    var response =
        CustomerResponse.builder().id(UUID.randomUUID()).mail("test@mail.com").name("name").build();

    given(customerSearch.list(any(Pageable.class)))
        .willReturn(new PageImpl<CustomerResponse>(List.of(response, response)));

    mockMvc
        .perform(get(CONTEXT_ROOT))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content.[0].id").value(response.getId().toString()))
        .andExpect(jsonPath("$.content.[0].mail").value(response.getMail()))
        .andExpect(jsonPath("$.content.[0].name").value(response.getName()))
        .andReturn();
  }
  //
  //  @Test
  //  public void shouldListFavorites() throws Exception {
  //    UUID customerId = UUID.randomUUID();
  //    List<UUID> productIds = List.of(UUID.randomUUID(), UUID.randomUUID());
  //
  //    given(favoriteSearch.list(eq(customerId), any(Pageable.class)))
  //        .willReturn(new PageImpl<>(productIds));
  //
  //    mockMvc
  //        .perform(get(CONTEXT_ROOT, customerId))
  //        .andExpect(status().isOk())
  //        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
  //        .andReturn();
  //  }
  //
  //  @Test
  //  public void shouldRemoveProductFavorite() throws Exception {
  //    var command =
  //        RemoveFavoriteCommand.builder()
  //            .productId(UUID.randomUUID())
  //            .customerId(UUID.randomUUID())
  //            .build();
  //
  //    mockMvc
  //        .perform(
  //            delete(CONTEXT_ROOT + "{productId}/", command.getCustomerId(),
  // command.getProductId()))
  //        .andExpect(status().isNoContent())
  //        .andReturn();
  //
  //    then(favoriteRemoval).should().remove(command);
  //  }

  @Test
  void update() {}
}
