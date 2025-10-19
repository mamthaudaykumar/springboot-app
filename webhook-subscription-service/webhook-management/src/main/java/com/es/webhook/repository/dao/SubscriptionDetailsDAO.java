package com.es.webhook.repository.dao;

import com.es.webhook.repository.model.SubscriptionDetails;
import com.es.webhook.repository.model.SubscriptionDetails.Destination;
import java.util.List;

public interface SubscriptionDetailsDAO {

  SubscriptionDetails findSubscriptionType(String id);

  void deleteByType(String subscriptionId, String subscriptionType);

  List<SubscriptionDetails> findAllBySubscriptionId(List<String> subscriptionIds);

  List<SubscriptionDetails> findAllBySubscriptionId(String subscriptionId);

  List<SubscriptionDetails> save(List<SubscriptionDetails> subscriptionDetailsList);

  void updateEnabledAndDestination(String id, boolean isEnable, List<Destination> destinations);
}
