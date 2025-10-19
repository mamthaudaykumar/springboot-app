package com.es.webhook.controller;

import com.es.webhook.admin.api.AdminApisApi;
import com.es.webhook.admin.model.PagedSubscriptionSummary;
import com.es.webhook.admin.model.SubscriptionRequest;
import com.es.webhook.admin.model.SubscriptionResponse;
import com.es.webhook.admin.model.SubscriptionSummary;
import com.es.webhook.admin.model.SubscriptionUpdateRequest;
import com.es.webhook.service.WebhookManagementService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookManagementController implements AdminApisApi {

  private final WebhookManagementService webhookManagementService;

  @Autowired
  public WebhookManagementController(WebhookManagementService webhookManagementService) {

    this.webhookManagementService = webhookManagementService;
  }

  public ResponseEntity<SubscriptionResponse> createSubscription(
      SubscriptionRequest subscriptionRequest) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(webhookManagementService.createSubscription(subscriptionRequest));
  }

  public ResponseEntity<Void> deleteSubscriptionById(String subscriptionId) {
    webhookManagementService.deleteSubscriptionsById(subscriptionId);
    return ResponseEntity.noContent().build();
  }

  public ResponseEntity<Void> deleteSubscriptionTypeById(
      String subscriptionId, String subscriptionTypeId) {
    webhookManagementService.deleteSubscriptionByType(subscriptionId, subscriptionTypeId);
    return ResponseEntity.noContent().build();
  }

  public ResponseEntity<List<SubscriptionSummary>> getSubscriptionById(String id) {
    return new ResponseEntity<>(webhookManagementService.findSubscriptionById(id), HttpStatus.OK);
  }

  public ResponseEntity<PagedSubscriptionSummary> listSubscriptions(Integer page, Integer size) {
    return new ResponseEntity<>(
        webhookManagementService.findAllSubscriptions(page, size), HttpStatus.OK);
  }

  public ResponseEntity<SubscriptionResponse> updateSubscription(
      String id, SubscriptionUpdateRequest subscriptionRequest) {
    return ResponseEntity.ok(webhookManagementService.updateSubscriptions(id, subscriptionRequest));
  }
}
