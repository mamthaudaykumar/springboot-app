package com.es.webhook.mapper;

import com.es.webhook.admin.model.SubscriptionRequest;
import com.es.webhook.admin.model.SubscriptionResponse;
import com.es.webhook.admin.model.SubscriptionResponseSubscriptionsInner;
import com.es.webhook.admin.model.SubscriptionSummary;
import com.es.webhook.admin.model.SubscriptionSummarySubscriptionsInner;
import com.es.webhook.repository.model.SubscriptionDetails;
import com.es.webhook.repository.model.WebhookSubscriptionConfig;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WebhookSubscriptionMapper {

  @Mapping(source = "id", target = "subscriptionId")
  SubscriptionSummary toSubscriptionSummary(WebhookSubscriptionConfig entity);

  WebhookSubscriptionConfig toWebhookSubscription(SubscriptionRequest request);

  List<SubscriptionResponseSubscriptionsInner> toSubscriptionsInnerResponse(
      List<SubscriptionDetails> subscriptionDetailsList);

  List<SubscriptionSummarySubscriptionsInner> toSubscriptionsSummaryInnerResponse(
      List<SubscriptionDetails> subscriptionDetailsList);

  //  @Mapping(source = "id", target = "subscriptionId")
  //  SubscriptionSummary toPostResponse(WebhookSubscriptionConfig entity);

  List<SubscriptionSummary> toPostResponseList(List<WebhookSubscriptionConfig> entity);

  @Mapping(source = "id", target = "subscriptionId")
  SubscriptionResponse toSubscriptionResponse(WebhookSubscriptionConfig entity);
}
