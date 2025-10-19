package com.es.webhook.service;

import com.es.webhook.admin.model.PagedSubscriptionSummary;
import com.es.webhook.admin.model.SubscriptionRequest;
import com.es.webhook.admin.model.SubscriptionResponse;
import com.es.webhook.admin.model.SubscriptionSummary;
import com.es.webhook.admin.model.SubscriptionUpdateRequest;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface WebhookManagementService {

  SubscriptionResponse createSubscription(SubscriptionRequest subscriptionRequest);

  PagedSubscriptionSummary findAllSubscriptions(Integer page, Integer size);

  List<SubscriptionSummary> findSubscriptionById(String id);

  void deleteSubscriptionsById(String id);

  @Transactional
  void deleteSubscriptionByType(String subscriptionId, String subscriptionTypeId);

  SubscriptionResponse updateSubscriptions(String id, SubscriptionUpdateRequest updateRequest);
}
