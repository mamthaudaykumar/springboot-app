package com.es.webhook.repository.dao;

import com.es.webhook.exception.DataNotFoundException;
import com.es.webhook.repository.model.SubscriptionDetails;
import com.es.webhook.repository.model.SubscriptionDetails.Destination;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionDetailsDAOImpl implements SubscriptionDetailsDAO {

  private final SubscriptionDetailsRepository sdRepository;
  private final MongoTemplate mongoTemplate;

  public SubscriptionDetailsDAOImpl(
      SubscriptionDetailsRepository sdRepository, MongoTemplate mongoTemplate) {
    this.sdRepository = sdRepository;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public SubscriptionDetails findSubscriptionType(String id) {
    return sdRepository
        .findById(id)
        .orElseThrow(
            () ->
                new DataNotFoundException(
                    String.format("SubscriptionDetails for ID: %s not found", id)));
  }

  @Override
  public void deleteByType(String subscriptionId, String subscriptionType) {
    sdRepository.deleteBySubscriptionIdAndSubscriptionType(subscriptionId, subscriptionType);
  }

  @Override
  public List<SubscriptionDetails> findAllBySubscriptionId(String subscriptionId) {
    return sdRepository.findBySubscriptionId(subscriptionId);
  }

  @Override
  public List<SubscriptionDetails> findAllBySubscriptionId(List<String> subscriptionIds) {
    return sdRepository.findBySubscriptionIdIn(subscriptionIds);
  }

  @Override
  public List<SubscriptionDetails> save(List<SubscriptionDetails> subscriptionDetailsList) {
    return sdRepository.saveAll(subscriptionDetailsList);
  }

  @Override
  public void updateEnabledAndDestination(
      String id, boolean isEnable, List<Destination> destinations) {
    Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));

    Update update =
        new Update()
            .set("enabled", isEnable)
            .set("destinations", destinations); // newDestination is a mapped object

    mongoTemplate.updateFirst(query, update, SubscriptionDetails.class);
  }
}
