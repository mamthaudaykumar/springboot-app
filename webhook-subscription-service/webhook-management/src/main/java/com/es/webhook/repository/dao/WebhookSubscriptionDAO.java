package com.es.webhook.repository.dao;

import com.es.webhook.repository.model.WebhookSubscriptionConfig;
import com.es.webhook.repository.model.WebhookSubscriptionConfig.Subscriptions;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface WebhookSubscriptionDAO {

  WebhookSubscriptionConfig create(WebhookSubscriptionConfig subscription);

  Page<WebhookSubscriptionConfig> findAll(int pageNumber, int pageSize);

  List<WebhookSubscriptionConfig> findAll();

  int totalDocumentCount();

  Optional<WebhookSubscriptionConfig> findById(String id);

  WebhookSubscriptionConfig update(String id, WebhookSubscriptionConfig updated);

  void deleteById(String id);

  void deleteDocumentSubscriptions(String id, String subTypeId);

  void saveSubscriptionDetails(String id, List<Subscriptions> subscriptions);
}
