package com.es.webhook.service;

import com.es.webhook.admin.model.PagedSubscriptionSummary;
import com.es.webhook.admin.model.SubscriptionRequest;
import com.es.webhook.admin.model.SubscriptionResponse;
import com.es.webhook.admin.model.SubscriptionResponseSubscriptionsInner;
import com.es.webhook.admin.model.SubscriptionSummary;
import com.es.webhook.admin.model.SubscriptionUpdateRequest;
import com.es.webhook.config.CfgAppProperties;
import com.es.webhook.exception.DataNotFoundException;
import com.es.webhook.mapper.SubscriptionDetailsMapper;
import com.es.webhook.mapper.WebhookSubscriptionMapper;
import com.es.webhook.repository.dao.SubscriptionDetailsDAO;
import com.es.webhook.repository.dao.WebhookSubscriptionDAO;
import com.es.webhook.repository.model.SubscriptionDetails;
import com.es.webhook.repository.model.SubscriptionDetails.Destination;
import com.es.webhook.repository.model.WebhookSubscriptionConfig;
import com.es.webhook.repository.model.WebhookSubscriptionConfig.Subscriptions;
import com.es.webhook.util.AlgorithmEnum;
import com.es.webhook.util.SupportedAuthTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WebhookManagementServiceImpl implements WebhookManagementService {

  private final WebhookSubscriptionDAO webhookSubscriptionDAO;
  private final ObjectMapper objectMapper;
  private final WebhookSubscriptionMapper webhookSubscriptionMapper;
  private final CfgAppProperties cfgAppProperties;
  private final SubscriptionDetailsMapper subscriptionDetailsMapper;
  private final SubscriptionDetailsDAO subscriptionDetailsDAO;

  @Autowired
  public WebhookManagementServiceImpl(
      WebhookSubscriptionDAO webhookSubscriptionDAO,
      ObjectMapper objectMapper,
      WebhookSubscriptionMapper webhookSubscriptionMapper,
      CfgAppProperties cfgAppProperties,
      SubscriptionDetailsMapper subscriptionDetailsMapper,
      SubscriptionDetailsDAO subscriptionDetailsDAO) {

    this.webhookSubscriptionDAO = webhookSubscriptionDAO;
    this.objectMapper = objectMapper;
    this.webhookSubscriptionMapper = webhookSubscriptionMapper;
    this.cfgAppProperties = cfgAppProperties;
    this.subscriptionDetailsMapper = subscriptionDetailsMapper;
    this.subscriptionDetailsDAO = subscriptionDetailsDAO;
  }

  @Override
  @Transactional
  public SubscriptionResponse createSubscription(SubscriptionRequest subscriptionRequest) {
    WebhookSubscriptionConfig subscriptionConfig =
        webhookSubscriptionMapper.toWebhookSubscription(subscriptionRequest);
    webhookSubscriptionDAO.create(subscriptionConfig);

    List<SubscriptionDetails> subscriptionDetails =
        subscriptionDetailsMapper.toSubscriptionDetails(
            subscriptionRequest.getWebhookConfig().getSubscriptions());

    subscriptionDetails.forEach(
        e -> {
          e.setSubscriptionId(subscriptionConfig.getId());
          e.setSubscriptionUrl(
              frameSubscriptionURL(
                  subscriptionConfig.getId(),
                  subscriptionConfig.getPlatform(),
                  e.getSubscriptionType()));
        });
    subscriptionDetailsDAO.save(subscriptionDetails);

    List<Subscriptions> subscriptionsList = new ArrayList<>();
    subscriptionDetails.stream()
        .forEach(
            e -> {
              subscriptionsList.add(new Subscriptions(e.getId(), e.getSubscriptionType()));
            });
    subscriptionConfig.setSubscriptions(subscriptionsList);
    webhookSubscriptionDAO.update(subscriptionConfig.getId(), subscriptionConfig);

    SubscriptionResponse response =
        webhookSubscriptionMapper.toSubscriptionResponse(subscriptionConfig);
    response.setSubscriptions(
        webhookSubscriptionMapper.toSubscriptionsInnerResponse(subscriptionDetails));
    return response;
  }

  @Override
  public PagedSubscriptionSummary findAllSubscriptions(Integer page, Integer size) {

    Page pagedDetails = webhookSubscriptionDAO.findAll(page, size);
    // Fetch all subscriptions and map to summaries
    List<SubscriptionSummary> subscriptionSummaries =
        webhookSubscriptionMapper.toPostResponseList(pagedDetails.getContent());

    subscriptionSummaries = subscriptionSummaries(subscriptionSummaries);
    PagedSubscriptionSummary pagedSubscriptionSummary = new PagedSubscriptionSummary();

    pagedSubscriptionSummary.totalElements((int) pagedDetails.getTotalElements());
    pagedSubscriptionSummary.totalPages(pagedDetails.getTotalPages());
    pagedSubscriptionSummary.pageNumber(page);
    pagedSubscriptionSummary.pageSize(size);
    pagedSubscriptionSummary.data(subscriptionSummaries);

    return pagedSubscriptionSummary;
  }

  private List<SubscriptionSummary> subscriptionSummaries(
      List<SubscriptionSummary> subscriptionSummaries) {

    // Extract subscription IDs
    List<String> ids =
        subscriptionSummaries.stream()
            .map(SubscriptionSummary::getSubscriptionId)
            .collect(Collectors.toList());
    // Fetch all details using IDs
    List<SubscriptionDetails> details = subscriptionDetailsDAO.findAllBySubscriptionId(ids);

    // Group details by subscription ID
    Map<String, List<SubscriptionDetails>> detailsMap =
        details.stream().collect(Collectors.groupingBy(SubscriptionDetails::getSubscriptionId));

    // Optionally, attach details to summaries (if needed)
    for (SubscriptionSummary summary : subscriptionSummaries) {
      List<SubscriptionDetails> summaryDetails =
          detailsMap.getOrDefault(summary.getSubscriptionId(), Collections.emptyList());
      summary.setSubscriptions(
          webhookSubscriptionMapper.toSubscriptionsSummaryInnerResponse(summaryDetails));
    }

    return subscriptionSummaries;
  }

  @Override
  public List<SubscriptionSummary> findSubscriptionById(String id) {
    List<WebhookSubscriptionConfig> subscription =
        webhookSubscriptionDAO
            .findById(id)
            .map(Collections::singletonList)
            .orElseThrow(() -> new DataNotFoundException("Subscription not found with id: " + id));
    List<SubscriptionSummary> summaries =
        subscription.stream()
            .map(webhookSubscriptionMapper::toSubscriptionSummary)
            .collect(Collectors.toList());
    return subscriptionSummaries(summaries);
  }

  @Override
  public void deleteSubscriptionsById(String id) {
    if (!webhookSubscriptionDAO.findById(id).isPresent()) {
      throw new DataNotFoundException(String.format("Subscription %s not found", id));
    }
    webhookSubscriptionDAO.deleteById(id);
  }

  @Transactional
  @Override
  public void deleteSubscriptionByType(String subscriptionId, String subscriptionTypeId) {
    WebhookSubscriptionConfig subscription =
        webhookSubscriptionDAO
            .findById(subscriptionId)
            .orElseThrow(
                () ->
                    new DataNotFoundException(
                        String.format("Subscription %s not found", subscriptionId)));

    subscriptionDetailsDAO.deleteByType(subscriptionId, subscriptionTypeId);
    webhookSubscriptionDAO.deleteDocumentSubscriptions(subscriptionId, subscriptionTypeId);
  }

  private String frameSubscriptionURL(String id, String platformName, String subscriptionType) {
    return String.format(
        "%s/subscription/%s/%s/%s",
        cfgAppProperties.getBaseURL(), id, platformName, subscriptionType);
  }

  public SubscriptionResponse updateSubscriptions(
      String id, SubscriptionUpdateRequest updateRequest) {

    WebhookSubscriptionConfig existing =
        webhookSubscriptionDAO
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Subscription " + id + " not found"));

    if (updateRequest.getPlatform() != null) existing.setPlatform(updateRequest.getPlatform());
    if (updateRequest.getName() != null) existing.setName(updateRequest.getName());
    if (updateRequest.getDescription() != null)
      existing.setDescription(updateRequest.getDescription());
    if (updateRequest.getEnabled() != null) existing.setEnabled(updateRequest.getEnabled());
    if (updateRequest.getChannelId() != null) existing.setChannelId(updateRequest.getChannelId());
    if (updateRequest.getDataDomain() != null)
      existing.setDataDomain(updateRequest.getDataDomain());

    // Update Auth details if provide in update request
    if (Optional.ofNullable(updateRequest.getAuthentication()).isPresent()) {
      if (updateRequest.getAuthentication().getType().equals(SupportedAuthTypeEnum.HMAC)) {
        existing
            .getAuthentication()
            .getConfig()
            .setHeaderName(updateRequest.getAuthentication().getConfig().getHeaderName());
        existing
            .getAuthentication()
            .getConfig()
            .setAlgorithm(
                AlgorithmEnum.fromValue(
                    updateRequest.getAuthentication().getConfig().getAlgorithm().getValue()));
        existing
            .getAuthentication()
            .getConfig()
            .setSecretKey(updateRequest.getAuthentication().getConfig().getSecretKey());
      }
    }

    List<SubscriptionResponseSubscriptionsInner> responsesSubscriptions = new ArrayList<>();
    List<Subscriptions> subscriptions = new ArrayList<>();
    updateRequest.getWebhookConfig().getSubscriptions().stream()
        .forEach(
            e -> {
              List<Destination> destinations =
                  subscriptionDetailsMapper.toDestinations(e.getDestinations());
              if (Optional.ofNullable(e.getSubscriptionTypeId()).isPresent()) {
                subscriptionDetailsDAO.updateEnabledAndDestination(
                    id, e.getEnabled(), destinations);

              } else {
                SubscriptionDetails data = subscriptionDetailsMapper.toSubscriptionItem(e);
                data.setSubscriptionUrl(
                    frameSubscriptionURL(
                        id, updateRequest.getPlatform(), data.getSubscriptionType()));
                data.setSubscriptionId(id);
                subscriptionDetailsDAO.save(Arrays.asList(data));
                subscriptions.add(new Subscriptions(data.getId(), data.getSubscriptionType()));
                responsesSubscriptions.add(subscriptionDetailsMapper.toUpdateResponse(data));
              }
            });
    existing.setSubscriptions(subscriptions);
    webhookSubscriptionDAO.saveSubscriptionDetails(id, subscriptions);
    SubscriptionResponse response = webhookSubscriptionMapper.toSubscriptionResponse(existing);
    response.setSubscriptions(responsesSubscriptions);
    return response;
  }
}
