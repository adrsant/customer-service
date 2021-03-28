package com.luizalabs.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class WiremockExtension extends WireMockServer
    implements AfterAllCallback, BeforeAllCallback, AfterEachCallback {

  public static final String MAPPINGS_DIRECTORY = "wiremock-mappings";
  public static final Integer SERVER_PORT = 8085;

  public WiremockExtension() {
    this(
        WireMockConfiguration.wireMockConfig()
            .withRootDirectory(MAPPINGS_DIRECTORY)
            .port(SERVER_PORT));
  }

  public WiremockExtension(Options options) {
    super(options);
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    WireMock.reset();
  }

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    this.start();
    WireMock.configureFor("localhost", SERVER_PORT);
    System.setProperty("WIREMOCK_PORT", SERVER_PORT.toString());
  }

  @Override
  public void afterAll(ExtensionContext extensionContext) {
    this.stop();
  }
}
