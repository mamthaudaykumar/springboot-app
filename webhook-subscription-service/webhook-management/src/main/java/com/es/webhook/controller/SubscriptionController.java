package com.es.webhook.controller;

import com.es.webhook.admin.api.SubscriptionApi;
import com.es.webhook.admin.model.TriggerWebhookRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController implements SubscriptionApi {
  public ResponseEntity<Void> triggerWebhook(
      String subscriptionId,
      String platformName,
      String subscriptionType,
      TriggerWebhookRequest triggerWebhookRequest) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
