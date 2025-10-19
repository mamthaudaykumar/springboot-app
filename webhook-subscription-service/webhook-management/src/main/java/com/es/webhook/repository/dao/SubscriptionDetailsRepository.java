package com.es.webhook.repository.dao;

import com.es.webhook.repository.model.SubscriptionDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface SubscriptionDetailsRepository
    extends MongoRepository<SubscriptionDetails, String> {

  @Query(value = "SELECT a FROM USER a WHRE a.id = :abc ")
  Optional<SubscriptionDetails> findBySubscriptionIdAndSubscriptionType(
      String subscriptionId, String subscriptionType);

  List<SubscriptionDetails> findBySubscriptionId(String subscriptionId);

  List<SubscriptionDetails> findBySubscriptionIdIn(List<String> subscriptionId);

  List<SubscriptionDetails> findByEnabledTrue();

  void deleteBySubscriptionIdAndSubscriptionType(String subscriptionId, String subscriptionType);
}
