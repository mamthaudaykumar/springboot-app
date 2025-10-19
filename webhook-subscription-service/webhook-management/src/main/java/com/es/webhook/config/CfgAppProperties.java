package com.es.webhook.config;

import jakarta.annotation.PostConstruct;
import java.util.Optional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CfgAppProperties {

  @Getter
  @Value("${spring.application.name}")
  private String applicationName;

  @Getter
  @Value("${spring.data.mongodb.uri}")
  private String uri;

  @Getter
  @Value("${spring.data.mongodb.database}")
  private String database;

  @Getter
  @Value("${oms.baseURL}")
  private String baseURL;

  @Getter
  @Value("${AWS.ACCESS_KEY_ID}")
  private String awsAccessKey;

  @Getter
  @Value("${AWS.SECRET_ACCESS_KEY}")
  private String awsSecretKey;

  @Getter
  @Value("${AWS.WEBHOOK_SQS_URL}")
  private String webhookSQSUrl;

  @Getter
  @Value("${AWS.REGION}")
  private String region;

  @PostConstruct
  public void validate() {
    Optional.ofNullable(baseURL)
        .filter(val -> !val.isBlank())
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "Property 'oms.baseURL' is required but missing or blank."));

    Optional.ofNullable(uri)
        .filter(val -> !val.isBlank())
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "Property 'spring.data.mongodb.uri' is required but missing or blank."));
  }
}
