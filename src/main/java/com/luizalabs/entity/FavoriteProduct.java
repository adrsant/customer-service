package com.luizalabs.entity;

import com.luizalabs.entity.pk.FavoriteProductPk;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
@EqualsAndHashCode
@IdClass(FavoriteProductPk.class)
public class FavoriteProduct {

  @Id private UUID customerId;
  @Id private UUID productId;
}
