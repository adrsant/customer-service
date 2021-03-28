package com.luizalabs.interaction.favority.query;

import java.util.UUID;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public class GetFavoritesQueryParam {

  private final UUID productId;
  private final UUID customerId;
  private final Pageable pageable;
}
