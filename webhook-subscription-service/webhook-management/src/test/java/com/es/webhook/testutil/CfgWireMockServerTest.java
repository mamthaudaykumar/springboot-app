package com.es.webhook.testutil;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CfgWireMockServerTest {

  private static final Logger logger = LoggerFactory.getLogger(CfgWireMockServerTest.class);

  @Value("${test.wiremock.port:8080}")
  private int port;

  @Bean
  public WireMockServer wireMockServer() {
    logger.info("Starting WireMock server on port {}", port);
    WireMockServer wireMockServer =
        new WireMockServer(WireMockConfiguration.wireMockConfig().port(port));

    return wireMockServer;
  }
}
