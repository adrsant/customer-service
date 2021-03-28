package com.luizalabs.interaction.favority;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.luizalabs.entity.FavoriteProduct;
import com.luizalabs.repository.FavoriteProductRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class FavoriteSearchTest {

  @Mock private FavoriteProductRepository repository;
  @InjectMocks private FavoriteSearch interaction;

  @Test
  void shouldGetFavoritePageable() {
    var customerId = UUID.randomUUID();
    var productId = UUID.randomUUID();
    var pageRequest = PageRequest.of(1, 10);
    var favoriteProduct = mock(FavoriteProduct.class);
    given(favoriteProduct.getProductId()).willReturn(productId);
    PageImpl pageResult = new PageImpl(List.of(favoriteProduct));
    given(repository.findByCustomerId(customerId, pageRequest)).willReturn(pageResult);

    Page<UUID> page = interaction.list(customerId, pageRequest);

    assertThat(page.get()).containsOnly(productId);
  }
}
