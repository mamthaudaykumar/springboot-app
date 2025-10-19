package com.es.webhook.repository.model;

import com.es.webhook.util.AlgorithmEnum;
import com.es.webhook.util.SupportedAuthTypeEnum;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "webhookSubscriptionConfig")
public class WebhookSubscriptionConfig extends Auditable {

  @Id private String id;

  private String platform;
  private String name;
  private String description;
  private Boolean enabled;
  private String channelId;
  private List<String> dataDomain;

  private Authentication authentication;
  private List<Subscriptions> subscriptions;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Authentication {
    private SupportedAuthTypeEnum type;
    private AuthConfig config;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AuthConfig {
    private String secretKey;
    private String headerName;
    private AlgorithmEnum algorithm;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Subscriptions {
    private String subscriptionTypeId;
    private String subscriptionType;
  }
}
