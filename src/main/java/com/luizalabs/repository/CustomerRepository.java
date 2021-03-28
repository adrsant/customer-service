package com.luizalabs.repository;

import com.luizalabs.entity.Customer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  boolean existsByMail(String mail);

  boolean existsByMailAndIdNot(String mail, UUID uuid);
}
