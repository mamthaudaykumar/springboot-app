package com.es.webhook.repository.dao;

import com.es.webhook.repository.model.WebhookSubscriptionConfig;
import com.es.webhook.repository.model.WebhookSubscriptionConfig.Subscriptions;
import com.mongodb.BasicDBObject;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class WebhookSubscriptionDAOImpl implements WebhookSubscriptionDAO {

  private WebhookSubscriptionRepository wsRepository;

  private final MongoTemplate mongoTemplate;

  @Autowired
  public WebhookSubscriptionDAOImpl(
      WebhookSubscriptionRepository webhookSubscriptionRepository, MongoTemplate mongoTemplate) {
    this.wsRepository = webhookSubscriptionRepository;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public WebhookSubscriptionConfig create(WebhookSubscriptionConfig subscription) {
    return wsRepository.save(subscription);
  }

  @Override
  public Page<WebhookSubscriptionConfig> findAll(int pageNumber, int pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return wsRepository.findAll(pageable);
  }

  @Override
  public List<WebhookSubscriptionConfig> findAll() {
    return wsRepository.findAll();
  }

  @Override
  public int totalDocumentCount() {
    return wsRepository.findAll().size();
  }

  @Override
  public Optional<WebhookSubscriptionConfig> findById(String id) {
    return wsRepository.findById(id);
  }

  @Override
  public WebhookSubscriptionConfig update(String id, WebhookSubscriptionConfig updated) {
    return wsRepository
        .findById(id)
        .map(
            existing -> {
              updated.setId(id);
              return wsRepository.save(updated);
            })
        .orElseThrow(() -> new RuntimeException("Subscription not found"));
  }

  @Override
  public void deleteById(String id) {
    if (!wsRepository.existsById(id)) {
      throw new RuntimeException("Subscription not found");
    }
    wsRepository.deleteById(id);
  }

  @Override
  public void deleteDocumentSubscriptions(String id, String subTypeId) {
    Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));

    Update update =
        new Update().pull("subscriptions", new BasicDBObject("subscriptionTypeId", subTypeId));

    mongoTemplate.updateFirst(query, update, WebhookSubscriptionConfig.class);
  }

  @Override
  public void saveSubscriptionDetails(String id, List<Subscriptions> subscriptions) {
    Query query = new Query(Criteria.where("_id").is(id));
    Update update = new Update().addToSet("subscriptions").each(subscriptions);
    mongoTemplate.updateFirst(query, update, WebhookSubscriptionConfig.class);
  }
}
