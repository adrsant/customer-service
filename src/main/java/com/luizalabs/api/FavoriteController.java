package com.luizalabs.api;

import com.luizalabs.interaction.favority.FavoriteAddition;
import com.luizalabs.interaction.favority.FavoriteRemoval;
import com.luizalabs.interaction.favority.FavoriteSearch;
import com.luizalabs.interaction.favority.command.AddFavoriteCommand;
import com.luizalabs.interaction.favority.command.RemoveFavoriteCommand;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/{customerId}/favorite/")
public class FavoriteController {

  private final FavoriteAddition favoriteAddition;
  private final FavoriteRemoval favoriteRemoval;
  private final FavoriteSearch favoriteSearch;

  @PostMapping(path = "{productId}/")
  public void add(@PathVariable UUID customerId, @PathVariable UUID productId) {
    var command = AddFavoriteCommand.builder().customerId(customerId).productId(productId).build();
    favoriteAddition.add(command);
  }

  @DeleteMapping(path = "{productId}/")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID customerId, @PathVariable UUID productId) {
    var command =
        RemoveFavoriteCommand.builder().customerId(customerId).productId(productId).build();
    favoriteRemoval.remove(command);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<UUID> get(@PathVariable UUID customerId, @PageableDefault Pageable pageable) {
    return favoriteSearch.list(customerId, pageable);
  }
}
