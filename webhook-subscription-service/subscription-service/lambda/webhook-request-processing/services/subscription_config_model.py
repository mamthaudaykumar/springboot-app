from pydantic import BaseModel, Field
from typing import List, Optional
from enum import Enum
from bson import ObjectId


# Optional: Enum for allowed algorithm values
class HMACAlgorithm(str, Enum):
    HMACSHA256 = "HMACSHA256"
    HMACSHA384 = "HMACSHA384"
    HMACSHA512 = "HMACSHA512"


class AuthConfig(BaseModel):
    secretKey: str
    headerName: str
    algorithm: HMACAlgorithm


class Authentication(BaseModel):
    type: str  # You can also use Enum if limited to "HMAC"
    config: AuthConfig


class Subscription(BaseModel):
    subscriptionTypeId: str  # Use str if not deserializing ObjectId
    subscriptionType: str


class WebhookSubscriptionConfig(BaseModel):
    id: Optional[str] = Field(alias="_id")
    platform: str
    name: str
    description: str
    enabled: bool
    channelId: str
    dataDomain: List[str]
    authentication: Authentication
    subscriptions: List[Subscription]
    class_: Optional[str] = Field(alias="_class")

    class Config:
        allow_population_by_field_name = True
