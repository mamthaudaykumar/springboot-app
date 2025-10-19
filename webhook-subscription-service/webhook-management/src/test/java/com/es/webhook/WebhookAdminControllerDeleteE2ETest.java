package com.es.webhook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.es.webhook.admin.model.SubscriptionSummary;
import com.es.webhook.exception.DataNotFoundException;
import com.es.webhook.repository.model.WebhookSubscriptionConfig;
import com.es.webhook.testutil.AbstractCommonTest;
import com.es.webhook.testutil.CfgMongoTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({CfgMongoTest.class})
class WebhookAdminControllerDeleteE2ETest extends AbstractCommonTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private MongoTemplate mongoTemplate;

  @BeforeEach
  public void tearDown() {
    mongoTemplate.dropCollection(WebhookSubscriptionConfig.class);
  }

  @Test
  @Order(2)
  void testDeleteWebhookSubscriptionConfig() throws Exception {
    String SUBSCRIPTION_JSON =
        Files.readString(
            new ClassPathResource("webhookSubsPost.json").getFile().toPath(),
            StandardCharsets.UTF_8);

    // 1. Create subscription via POST first
    String postResponse =
        mockMvc
            .perform(
                post("/api/v1/admin/webhooks/subscriptions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(SUBSCRIPTION_JSON))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Extract subscriptionId from POST response
    String subscriptionId = objectMapper.readTree(postResponse).get("subscriptionId").asText();

    // 2. Perform DELETE by ID and validate fields except "destinations"
    mockMvc
        .perform(delete("/api/v1/admin/webhooks/subscriptions/{id}", subscriptionId))
        .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

    // 2. Perform GET by ID and it should not be found as it is deleted
    mockMvc
        .perform(get("/api/v1/admin/webhooks/subscriptions/{id}", subscriptionId))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(2)
  void testDeleteByIdByEventTypeWebhookSubscriptionConfig() throws Exception {
    String SUBSCRIPTION_JSON =
        Files.readString(
            new ClassPathResource("deleteTestWebhookSubsPost.json").getFile().toPath(),
            StandardCharsets.UTF_8);

    // 1. Create subscription via POST first
    String postResponse =
        mockMvc
            .perform(
                post("/api/v1/admin/webhooks/subscriptions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(SUBSCRIPTION_JSON))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Extract subscriptionId from POST response
    String subscriptionId = objectMapper.readTree(postResponse).get("subscriptionId").asText();

    mockMvc
        .perform(get("/api/v1/admin/webhooks/subscriptions/{id}", subscriptionId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].subscriptionId").value(subscriptionId));

    // 2. Perform DELETE by ID and validate fields except "destinations"
    mockMvc
        .perform(
            delete(
                "/api/v1/admin/webhooks/subscriptions/{id}/subscription_type/{eventType}",
                subscriptionId,
                "COMPANY_CREATED"))
        .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

    // 3. Perform GET by ID and it should not be found as it is deleted
    String getResponse =
        mockMvc
            .perform(get("/api/v1/admin/webhooks/subscriptions/{id}", subscriptionId))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<SubscriptionSummary> responses =
        objectMapper.readValue(getResponse, new TypeReference<List<SubscriptionSummary>>() {});
    responses.stream()
        .forEach(
            e -> {
              e.getSubscriptions()
                  .forEach(
                      j -> {
                        assertNotEquals("COMPANY_CREATED", j.getSubscriptionType());
                      });
            });
  }

  @Test
  @Order(2)
  void testDeleteSubscriptIdNotFound() throws Exception {

    // Extract subscriptionId from POST response
    String subscriptionId = "abc";

    // When performing DELETE, then expect 404 Not Found (or your custom error status)
    mockMvc
        .perform(delete("/api/v1/admin/webhooks/subscriptions/{id}", subscriptionId))
        .andExpect(status().isNotFound()) // Adjust based on your exception handling
        .andExpect(
            result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException))
        .andExpect(
            result ->
                assertEquals(
                    "Subscription abc not found", result.getResolvedException().getMessage()));
  }
}
