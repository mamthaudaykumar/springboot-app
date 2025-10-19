package com.es.webhook.aws.sqs;

import com.es.webhook.config.CfgAppProperties;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class SqsService {
  private final SqsClient sqsClient;
  private final CfgAppProperties cfgAppProperties;
  private final AwsClient awsClient;

  public SqsService(CfgAppProperties cfgAppProperties, AwsClient awsClient) {
    this.cfgAppProperties = cfgAppProperties;
    this.awsClient = awsClient;
    this.sqsClient =
        SqsClient.builder()
            .region(Region.of(cfgAppProperties.getRegion()))
            .credentialsProvider(awsClient.awsCredentialsProvider())
            .build();
  }

  public void sendMessage(String messageBody) {
    SendMessageRequest sendMsgRequest =
        SendMessageRequest.builder()
            .queueUrl(cfgAppProperties.getWebhookSQSUrl())
            .messageBody(messageBody)
            .build();

    sqsClient.sendMessage(sendMsgRequest);
  }
}
