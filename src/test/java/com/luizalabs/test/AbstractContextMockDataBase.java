package com.luizalabs.test;

abstract class AbstractContextMockDataBase {

  static final PostgresContainer POSTGRESQL_CONTAINER;

  static {
    POSTGRESQL_CONTAINER = PostgresContainer.getInstance();
    POSTGRESQL_CONTAINER.start();
  }

  protected AbstractContextMockDataBase() {}
}
