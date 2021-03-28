package com.luizalabs.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.luizalabs.api.handler.CustomGlobalExceptionHandler;
import com.luizalabs.interaction.favority.FavoriteAddition;
import com.luizalabs.interaction.favority.FavoriteRemoval;
import com.luizalabs.interaction.favority.FavoriteSearch;
import com.luizalabs.interaction.favority.command.AddFavoriteCommand;
import com.luizalabs.interaction.favority.command.RemoveFavoriteCommand;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class FavoriteControllerTest {

  private static final String CONTEXT_ROOT = "/api/customer/{customerId}/favorite/";
  @Mock private FavoriteAddition favoriteAddition;
  @Mock private FavoriteRemoval favoriteRemoval;
  @Mock private FavoriteSearch favoriteSearch;
  private MockMvc mockMvc;
  @InjectMocks private FavoriteController controller;

  @BeforeEach
  public void setup() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new CustomGlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
  }

  @Test
  public void shouldListFavorites() throws Exception {
    UUID customerId = UUID.randomUUID();
    List<UUID> productIds = List.of(UUID.randomUUID(), UUID.randomUUID());

    given(favoriteSearch.list(eq(customerId), any(Pageable.class)))
        .willReturn(new PageImpl<>(productIds));

    mockMvc
        .perform(get(CONTEXT_ROOT, customerId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Test
  public void shouldAddProductFavorite() throws Exception {
    var command =
        AddFavoriteCommand.builder()
            .productId(UUID.randomUUID())
            .customerId(UUID.randomUUID())
            .build();

    mockMvc
        .perform(
            post(CONTEXT_ROOT + "{productId}/", command.getCustomerId(), command.getProductId()))
        .andExpect(status().isOk())
        .andReturn();

    then(favoriteAddition).should().add(command);
  }

  @Test
  public void shouldRemoveProductFavorite() throws Exception {
    var command =
        RemoveFavoriteCommand.builder()
            .productId(UUID.randomUUID())
            .customerId(UUID.randomUUID())
            .build();

    mockMvc
        .perform(
            delete(CONTEXT_ROOT + "{productId}/", command.getCustomerId(), command.getProductId()))
        .andExpect(status().isNoContent())
        .andReturn();

    then(favoriteRemoval).should().remove(command);
  }
}
