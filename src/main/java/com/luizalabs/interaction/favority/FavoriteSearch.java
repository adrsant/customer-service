package com.luizalabs.interaction.favority;

import com.luizalabs.entity.FavoriteProduct;
import com.luizalabs.repository.FavoriteProductRepository;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FavoriteSearch {

  private final FavoriteProductRepository repository;

  public Page<UUID> list(@Valid @NotNull UUID customerId, Pageable pageable) {
    return repository.findByCustomerId(customerId, pageable).map(FavoriteProduct::getProductId);
  }
}
