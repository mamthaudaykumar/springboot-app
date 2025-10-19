package com.es.webhook.repository.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "subscriptionDetails")
@CompoundIndexes({
  @CompoundIndex(
      name = "unique_type_name",
      def = "{'subscriptionType': 1, 'subscriptionId': 1}",
      unique = true)
})
public class SubscriptionDetails extends Auditable {

  @Id private String id;
  private String subscriptionId;
  private String subscriptionType;
  private Boolean enabled;
  private String subscriptionUrl;
  private List<Destination> destinations;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Destination {
    private ScriptConfig scriptConfig;
    private RouteConfig routeConfig;
  }
}
