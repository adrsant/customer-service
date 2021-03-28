package com.luizalabs.test;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
public class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {

  private static final String IMAGE_VERSION = "postgres:11.2";
  private static PostgresContainer instance;

  private PostgresContainer() {
    super(IMAGE_VERSION);
  }

  public static PostgresContainer getInstance() {
    if (Objects.isNull(instance)) {
      instance = new PostgresContainer();
      instance.withReuse(true);
    }
    return instance;
  }

  @Override
  public void start() {
    super.start();
    System.setProperty("DB_CONTAINER_URL", instance.getJdbcUrl());
    System.setProperty("DB_CONTAINER_USERNAME", instance.getUsername());
    System.setProperty("DB_CONTAINER_PASSWORD", instance.getPassword());
  }

  @Override
  public void stop() {
    log.info("JVM handles shut down");
  }
}
