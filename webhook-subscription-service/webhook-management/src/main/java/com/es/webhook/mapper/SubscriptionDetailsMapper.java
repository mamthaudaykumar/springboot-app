package com.es.webhook.mapper;

import com.es.webhook.admin.model.Destination;
import com.es.webhook.admin.model.SubscriptionRequestWebhookConfigSubscriptionsInner;
import com.es.webhook.admin.model.SubscriptionResponseSubscriptionsInner;
import com.es.webhook.admin.model.SubscriptionUpdateRequestWebhookConfigSubscriptionsInner;
import com.es.webhook.repository.model.SubscriptionDetails;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionDetailsMapper {

  @Mapping(target = "destinations", source = "destinations")
  List<SubscriptionDetails> toSubscriptionDetails(
      List<SubscriptionRequestWebhookConfigSubscriptionsInner> subscriptionReq);

  @Mapping(target = "destinations", source = "destinations")
  SubscriptionDetails toSubscriptionItem(
      SubscriptionUpdateRequestWebhookConfigSubscriptionsInner item);

  @Mapping(target = "destinations", source = "destinations")
  List<SubscriptionDetails> toSubscriptionUpdateItem(
      List<SubscriptionUpdateRequestWebhookConfigSubscriptionsInner> item);

  List<SubscriptionDetails.Destination> toDestinations(
      List<Destination> destinations);

  SubscriptionResponseSubscriptionsInner toUpdateResponse(SubscriptionDetails subscriptionDetails);
}
