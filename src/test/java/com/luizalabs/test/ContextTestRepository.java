package com.luizalabs.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = {ContextTestRepository.Initializer.class})
public abstract class ContextTestRepository extends AbstractContextMockDataBase {

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of(
              "spring.datasource.url=" + System.getProperty("DB_CONTAINER_URL"),
              "spring.datasource.username=" + System.getProperty("DB_CONTAINER_USERNAME"),
              "spring.datasource.password=" + System.getProperty("DB_CONTAINER_PASSWORD"))
          .applyTo(configurableApplicationContext.getEnvironment());
    }
  }
}
