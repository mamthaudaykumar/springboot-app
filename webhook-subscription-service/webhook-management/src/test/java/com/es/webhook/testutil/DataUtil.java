package com.es.webhook.testutil;

public class DataUtil {

  // Missing platform
  public static final String MISSING_PLATFORM =
      """
    {
      "name": "CustomerCreationWebhook",
      "description": "Webhook for notifying customer creation events",
      "enabled": true,
      "channelId": "customer-events-channel",
      "dataDomain": ["customer"],
      "authentication": {
        "type": "HMAC",
        "config": {
          "secretKey": "superSecret",
          "headerName": "X-Signature",
          "algorithm": "HmacSHA256"
        }
      },
      "webhookConfig": {
        "subscriptions": [
          {
            "subscriptionType": "CUSTOMER_CREATED",
            "enabled": true,
            "destinations": []
          }
        ]
      }
    }
    """;

  // Missing name
  public static final String MISSING_NAME =
      """
    {
      "platform": "eCommerce",
      "description": "Webhook for notifying customer creation events",
      "enabled": true,
      "channelId": "customer-events-channel",
      "dataDomain": ["customer"],
      "authentication": {
        "type": "HMAC",
        "config": {
          "secretKey": "superSecret",
          "headerName": "X-Signature",
          "algorithm": "HmacSHA256"
        }
      },
      "webhookConfig": {
        "subscriptions": [
          {
            "subscriptionType": "CUSTOMER_CREATED",
            "enabled": true,
            "destinations": []
          }
        ]
      }
    }
    """;

  // Missing enabled
  public static final String MISSING_ENABLED =
      """
    {
      "platform": "eCommerce",
      "name": "CustomerCreationWebhook",
      "description": "Webhook for notifying customer creation events",
      "channelId": "customer-events-channel",
      "dataDomain": ["customer"],
      "authentication": {
        "type": "HMAC",
        "config": {
          "secretKey": "superSecret",
          "headerName": "X-Signature",
          "algorithm": "HmacSHA256"
        }
      },
      "webhookConfig": {
        "subscriptions": [
          {
            "subscriptionType": "CUSTOMER_CREATED",
            "enabled": true,
            "destinations": []
          }
        ]
      }
    }
    """;

  // Missing channelId
  public static final String MISSING_CHANNEL_ID =
      """
    {
      "platform": "eCommerce",
      "name": "CustomerCreationWebhook",
      "description": "Webhook for notifying customer creation events",
      "enabled": true,
      "dataDomain": ["customer"],
      "authentication": {
        "type": "HMAC",
        "config": {
          "secretKey": "superSecret",
          "headerName": "X-Signature",
          "algorithm": "HmacSHA256"
        }
      },
      "webhookConfig": {
        "subscriptions": [
          {
            "subscriptionType": "CUSTOMER_CREATED",
            "enabled": true,
            "destinations": []
          }
        ]
      }
    }
    """;

  // Missing dataDomain
  public static final String MISSING_DATA_DOMAIN =
      """
    {
      "platform": "eCommerce",
      "name": "CustomerCreationWebhook",
      "description": "Webhook for notifying customer creation events",
      "enabled": true,
      "channelId": "customer-events-channel",
      "authentication": {
        "type": "HMAC",
        "config": {
          "secretKey": "superSecret",
          "headerName": "X-Signature",
          "algorithm": "HmacSHA256"
        }
      },
      "webhookConfig": {
        "subscriptions": [
          {
            "subscriptionType": "CUSTOMER_CREATED",
            "enabled": true,
            "destinations": []
          }
        ]
      }
    }
    """;

  // Missing authentication
  public static final String MISSING_AUTHENTICATION =
      """
    {
      "platform": "eCommerce",
      "name": "CustomerCreationWebhook",
      "description": "Webhook for notifying customer creation events",
      "enabled": true,
      "channelId": "customer-events-channel",
      "dataDomain": ["customer"],
      "webhookConfig": {
        "subscriptions": [
          {
            "subscriptionType": "CUSTOMER_CREATED",
            "enabled": true,
            "destinations": []
          }
        ]
      }
    }
    """;

  // Missing webhookConfig
  public static final String MISSING_WEBHOOK_CONFIG =
      """
    {
      "platform": "eCommerce",
      "name": "CustomerCreationWebhook",
      "description": "Webhook for notifying customer creation events",
      "enabled": true,
      "channelId": "customer-events-channel",
      "dataDomain": ["customer"],
      "authentication": {
        "type": "HMAC",
        "config": {
          "secretKey": "superSecret",
          "headerName": "X-Signature",
          "algorithm": "HmacSHA256"
        }
      }
    }
    """;
}
