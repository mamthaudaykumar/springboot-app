from .mongo_connection import mongo_connection
from .subscription_config_model import WebhookSubscriptionConfig

COLLECTION_NAME = "subscriptionDetails"

def get_subscription_config_details(subscription_id):
    db = mongo_connection()
    collection = db[COLLECTION_NAME]
    doc = collection.find_one({"subscriptionId": subscription_id})
    data = WebhookSubscriptionConfig(**doc)
    return data
