package com.es.webhook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.eis"})
public class WebhookManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebhookManagementApplication.class, args);
  }
}
