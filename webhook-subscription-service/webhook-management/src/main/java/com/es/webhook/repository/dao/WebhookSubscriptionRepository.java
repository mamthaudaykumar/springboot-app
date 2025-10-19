package com.es.webhook.repository.dao;

import com.es.webhook.repository.model.WebhookSubscriptionConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WebhookSubscriptionRepository
    extends MongoRepository<WebhookSubscriptionConfig, String> {}
