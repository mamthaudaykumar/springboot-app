import logging
from services.subscription_config_details_dao import get_subscription_config_details
from services.hmac import verify_hmac

logger = logging.getLogger()
logger.setLevel(logging.INFO)

def lambda_handler(event, context):
    headers = {k.lower(): v for k, v in event.get("headers", {}).items()}
    payload = event.get("body", "")

    # Extract from path: /api/v1/webhook/subscription/{subscriptionId}/platform/{platformName}/{subscriptionType}
    path_parts = event.get("path", "").strip("/").split("/")

    try:
        subscription_index = path_parts.index("subscription")
        subscription_id = path_parts[subscription_index + 1]
        platform_name = path_parts[subscription_index + 3]  # 'platform/{platformName}'
        subscription_type = path_parts[subscription_index + 4]
    except (ValueError, IndexError):
        return {"statusCode": 400, "body": "Invalid path format"}

    subscription_config_details = get_subscription_config_details(subscription_id)
    if not subscription_config_details:
        return {"statusCode": 404, "body": "Subscription not found"}

    auth_details = subscription_config_details.authentication.config
    header_name = auth_details.headerName
    received_signature = headers.get(header_name)

    if not received_signature:
        return {"statusCode": 400, "body": "Missing signature header"}

    if not verify_hmac(auth_details.secretKey,
                       auth_details.algorithm,
                       payload,
                       received_signature):
        return {"statusCode": 403, "body": "Invalid signature"}

    return {"statusCode": 200, "body": "Signature verified"}
