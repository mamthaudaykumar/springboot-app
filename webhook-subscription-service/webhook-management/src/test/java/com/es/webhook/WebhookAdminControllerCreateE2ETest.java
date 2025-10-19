package com.es.webhook;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.es.webhook.repository.model.WebhookSubscriptionConfig;
import com.es.webhook.testutil.AbstractCommonTest;
import com.es.webhook.testutil.CfgMongoTest;
import com.es.webhook.testutil.DataUtil;
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
class WebhookAdminControllerCreateE2ETest extends AbstractCommonTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private MongoTemplate mongoTemplate;

  @BeforeEach
  public void tearDown() {
    mongoTemplate.dropCollection(WebhookSubscriptionConfig.class);
  }

  @Test
  @Order(1)
  void testCreateWebhookSubscriptionConfig() throws Exception {
    String json =
        Files.readString(
            new ClassPathResource("webhookSubsPost.json").getFile().toPath(),
            StandardCharsets.UTF_8);

    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.subscriptionId").exists())
        .andExpect(jsonPath("$.name").value("CustomerCreationWebhook"));
  }

  @Test
  @Order(1)
  void testCreateWebhookSubscriptionConfig_FullAssertions() throws Exception {
    String json =
        Files.readString(
            new ClassPathResource("webhookSubsPost.json").getFile().toPath(),
            StandardCharsets.UTF_8);

    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isCreated())
        // Top-level fields
        .andExpect(jsonPath("$.subscriptionId").exists())
        .andExpect(jsonPath("$.platform").value("eCommerce"))
        .andExpect(jsonPath("$.name").value("CustomerCreationWebhook"))
        .andExpect(
            jsonPath("$.description").value("Webhook for notifying customer creation events"))
        .andExpect(jsonPath("$.channelId").value("customer-events-channel"))
        .andExpect(jsonPath("$.dataDomain").value("customer"))
        .andExpect(jsonPath("$.enabled").value(true))

        // Subscriptions block
        .andExpect(jsonPath("$.subscriptions[0].enabled").value(true))
        .andExpect(jsonPath("$.subscriptions[0].subscriptionType").value("CUSTOMER_CREATED"))

        // Destination - API Config
        .andExpect(jsonPath("$.subscriptions[0].subscriptionUrl").exists());
  }

  @Test
  @Order(2)
  void testCreateWebhookSubscriptionConfig_MissingMandatoryField_ShouldReturn400()
      throws Exception {
    String invalidJson =
        """
      {
        "name": "MissingPlatform",
        "enabled": true
      }
      """;

    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(3)
  void testCreateWebhookSubscriptionConfig_InvalidAlgorithm_ShouldReturn400() throws Exception {
    String invalidJson =
        Files.readString(
                new ClassPathResource("webhookSubsPost.json").getFile().toPath(),
                StandardCharsets.UTF_8)
            .replace("HmacSHA256", "INVALID_ALGO");

    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(1)
  void testCreateSubscriptionRequest_allMandatoryFieldsPresent_shouldReturn201AndCorrectResponse()
      throws Exception {
    // Load request payload from resource file
    String json =
        Files.readString(
            new ClassPathResource("webhookSubsPost.json").getFile().toPath(),
            StandardCharsets.UTF_8);

    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.subscriptionId").exists())

        // Top-level fields
        .andExpect(jsonPath("$.platform").value("eCommerce"))
        .andExpect(jsonPath("$.name").value("CustomerCreationWebhook"))
        .andExpect(
            jsonPath("$.description").value("Webhook for notifying customer creation events"))
        .andExpect(jsonPath("$.channelId").value("customer-events-channel"))
        .andExpect(jsonPath("$.dataDomain").value("customer"))
        .andExpect(jsonPath("$.enabled").value(true))

        // WebhookConfig
        .andExpect(jsonPath("$.subscriptions").isArray())
        .andExpect(jsonPath("$.subscriptions[0].subscriptionType").value("CUSTOMER_CREATED"))
        .andExpect(jsonPath("$.subscriptions[0].enabled").value(true))
        .andExpect(jsonPath("$.subscriptions[0].subscriptionUrl").exists());
  }

  @Test
  @Order(2)
  void testCreateSubscriptionRequest_missingPlatform_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataUtil.MISSING_PLATFORM))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(3)
  void testCreateSubscriptionRequest_missingName_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataUtil.MISSING_NAME))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(4)
  void testCreateSubscriptionRequest_missingEnabled_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataUtil.MISSING_ENABLED))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(5)
  void testCreateSubscriptionRequest_missingChannelId_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataUtil.MISSING_CHANNEL_ID))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(6)
  void testCreateSubscriptionRequest_missingDataDomain_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataUtil.MISSING_DATA_DOMAIN))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(7)
  void testCreateSubscriptionRequest_missingAuthentication_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataUtil.MISSING_AUTHENTICATION))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(8)
  void testCreateSubscriptionRequest_missingWebhookConfig_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/admin/webhooks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataUtil.MISSING_WEBHOOK_CONFIG))
        .andExpect(status().isBadRequest());
  }
}
