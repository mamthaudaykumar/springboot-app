package com.es.webhook.aws.sqs;

import com.es.webhook.config.CfgAppProperties;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Component
public class AwsClient {

  private final CfgAppProperties cfgAppProperties;

  public AwsClient(CfgAppProperties cfgAppProperties) {
    this.cfgAppProperties = cfgAppProperties;
  }

  public AwsCredentialsProvider awsCredentialsProvider() {
    AwsBasicCredentials credentials =
        AwsBasicCredentials.create(
            cfgAppProperties.getAwsAccessKey(), cfgAppProperties.getAwsSecretKey());
    return StaticCredentialsProvider.create(credentials);
  }
}
