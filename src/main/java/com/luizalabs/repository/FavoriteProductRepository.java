package com.luizalabs.repository;

import com.luizalabs.entity.FavoriteProduct;
import com.luizalabs.entity.pk.FavoriteProductPk;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteProductRepository
    extends JpaRepository<FavoriteProduct, FavoriteProductPk> {

  Page<FavoriteProduct> findByCustomerId(UUID customerId, Pageable pageable);
}
