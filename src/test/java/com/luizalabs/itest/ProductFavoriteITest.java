package com.luizalabs.itest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.luizalabs.entity.pk.FavoriteProductPk;
import com.luizalabs.interaction.favority.command.AddFavoriteCommand;
import com.luizalabs.interaction.favority.command.RemoveFavoriteCommand;
import com.luizalabs.repository.FavoriteProductRepository;
import com.luizalabs.test.ContextTestMockAll;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public class ProductFavoriteITest extends ContextTestMockAll {

  private static final String CONTEXT_ROOT = "/api/customer/{customerId}/favorite/";
  @Autowired private MockMvc mockMvc;
  @Autowired private FavoriteProductRepository repository;

  @Test
  @Sql(scripts = "classpath:test-data/create-favorites.sql")
  public void shouldListFavorites() throws Exception {
    UUID customerId = UUID.fromString("6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11");

    mockMvc
        .perform(get(CONTEXT_ROOT, customerId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content.*", hasSize(1)))
        .andReturn();
  }

  @Test
  @Sql(scripts = "classpath:test-data/create-customers.sql")
  public void shouldAddProductFavorite() throws Exception {
    var command =
        AddFavoriteCommand.builder()
            .productId(UUID.fromString("1bf0f365-fbdd-4e21-9786-da459d78dd1f"))
            .customerId(UUID.fromString("6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11"))
            .build();

    mockMvc
        .perform(
            post(CONTEXT_ROOT + "{productId}/", command.getCustomerId(), command.getProductId()))
        .andExpect(status().isOk())
        .andReturn();

    var pk =
        FavoriteProductPk.builder()
            .productId(command.getProductId())
            .customerId(command.getCustomerId())
            .build();

    assertThat(repository.existsById(pk)).isTrue();
  }

  @Test
  @Sql(scripts = "classpath:test-data/create-customers.sql")
  public void shouldDoNotAddProductFavoriteBecauseNotExistsProduct() throws Exception {
    var command =
        AddFavoriteCommand.builder()
            .productId(UUID.fromString("5d0eb672-77ab-4b94-b5a7-b1e7d63af239"))
            .customerId(UUID.fromString("6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11"))
            .build();

    mockMvc
        .perform(
            post(CONTEXT_ROOT + "{productId}/", command.getCustomerId(), command.getProductId()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errors.[0]").value("The product not exists"))
        .andReturn();

    assertThat(repository.count()).isEqualTo(0);
  }

  @Test
  @Sql(scripts = "classpath:test-data/create-customers.sql")
  public void shouldDoNotAddProductFavoriteBecauseNotExistsCustomer() throws Exception {
    var command =
        AddFavoriteCommand.builder()
            .productId(UUID.fromString("1bf0f365-fbdd-4e21-9786-da459d78dd1f"))
            .customerId(UUID.randomUUID())
            .build();

    mockMvc
        .perform(
            post(CONTEXT_ROOT + "{productId}/", command.getCustomerId(), command.getProductId()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errors.[0]").value("The client not exists"))
        .andReturn();

    assertThat(repository.count()).isEqualTo(0);
  }

  @Test
  @Sql(scripts = "classpath:test-data/create-favorites.sql")
  public void shouldRemoveProductFavorite() throws Exception {
    var command =
        RemoveFavoriteCommand.builder()
            .productId(UUID.fromString("1bf0f365-fbdd-4e21-9786-da459d78dd1f"))
            .customerId(UUID.fromString("6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11"))
            .build();

    mockMvc
        .perform(
            delete(CONTEXT_ROOT + "{productId}/", command.getCustomerId(), command.getProductId()))
        .andExpect(status().isNoContent())
        .andReturn();

    var pk =
        FavoriteProductPk.builder()
            .productId(command.getProductId())
            .customerId(command.getCustomerId())
            .build();

    assertThat(repository.existsById(pk)).isFalse();
  }
}
