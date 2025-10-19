package com.es.webhook.testutil;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.testcontainers.containers.MongoDBContainer;

public class CfgMongoTest {
  public MongoDatabaseFactory mongoDatabaseFactory() {

    MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");
    mongoDBContainer.start();
    MongoDatabaseFactory factory =
        new SimpleMongoClientDatabaseFactory(
            MongoClients.create(mongoDBContainer.getReplicaSetUrl()), "testdb");
    return factory;
  }

  public MongoClient mongoClient() {
    MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");
    mongoDBContainer.start();
    return MongoClients.create(mongoDBContainer.getReplicaSetUrl());
  }

  @Bean
  public MongoTemplate mongoTemplate() {
    return new MongoTemplate(mongoClient(), "testdb");
  }
}
