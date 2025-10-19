package com.es.webhook;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.es.webhook.repository.model.WebhookSubscriptionConfig;
import com.es.webhook.testutil.AbstractCommonTest;
import com.es.webhook.testutil.CfgMongoTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({CfgMongoTest.class})
class WebhookAdminControllerGetE2ETest extends AbstractCommonTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private MongoTemplate mongoTemplate;

  @BeforeEach
  public void tearDown() {
    mongoTemplate.dropCollection(WebhookSubscriptionConfig.class);
  }

  @Test
  @Order(2)
  void testGetWebhookSubscriptionConfig() throws Exception {
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
            .andExpect(jsonPath("$.subscriptionId").exists())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Extract subscriptionId from POST response
    String subscriptionId = objectMapper.readTree(postResponse).get("subscriptionId").asText();

    // 2. Perform GET by ID and validate fields except "destinations"
    mockMvc
        .perform(get("/api/v1/admin/webhooks/subscriptions/{id}", subscriptionId))
        .andExpect(status().isOk())

        // Validate top-level fields
        .andExpect(jsonPath("$[0].subscriptionId").value(subscriptionId))
        .andExpect(jsonPath("$[0].platform").value("eCommerce"))
        .andExpect(jsonPath("$[0].name").value("CustomerCreationWebhook"))
        .andExpect(
            jsonPath("$[0].description").value("Webhook for notifying customer creation events"))
        .andExpect(jsonPath("$[0].enabled").value(true))
        .andExpect(jsonPath("$[0].channelId").value("customer-events-channel"))
        .andExpect(jsonPath("$[0].dataDomain").value("customer"))

        // Validate authentication fields
        .andExpect(jsonPath("$[0].authentication.type").value("HMAC"))
        .andExpect(jsonPath("$[0].authentication.config.secretKey").value("a1b2c3d4e5f6g7h8i9j0"))
        .andExpect(jsonPath("$[0].authentication.config.headerName").value("X-Signature"))
        .andExpect(jsonPath("$[0].authentication.config.algorithm").value("HMACSHA256"))

        // Validate webhookConfig subscriptions array fields except destinations
        .andExpect(jsonPath("$[0].subscriptions[0].subscriptionType").value("CUSTOMER_CREATED"))
        .andExpect(jsonPath("$[0].subscriptions[0].enabled").value(true));
    // destinations validation intentionally skipped
  }

  @Test
  @Order(2)
  void testGetAllWebhookSubscriptionConfig() throws Exception {
    String SUBSCRIPTION_JSON =
        Files.readString(
            new ClassPathResource("webhookSubsPost.json").getFile().toPath(),
            StandardCharsets.UTF_8);

    // 1. Create subscription via POST - 2 records created
    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(SUBSCRIPTION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.subscriptionId").exists())
        .andReturn()
        .getResponse()
        .getContentAsString();
    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(SUBSCRIPTION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.subscriptionId").exists())
        .andReturn()
        .getResponse()
        .getContentAsString();

    // 2. Perform GET by ID and validate fields except "destinations"
    mockMvc
        .perform(get("/api/v1/admin/webhooks/subscriptions"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements", equalTo(2)));
  }
}
