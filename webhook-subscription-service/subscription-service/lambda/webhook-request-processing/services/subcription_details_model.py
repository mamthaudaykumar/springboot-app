from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any

# Support MongoDB ObjectId as a string
class PyObjectId(str):
    @classmethod
    def __get_validators__(cls):
        yield cls.validate

    @classmethod
    def validate(cls, v):
        if isinstance(v, dict) and "$oid" in v:
            return v["$oid"]
        if isinstance(v, str):
            return v
        raise TypeError("Invalid ObjectId format")

# Nested models for your document structure

class ScriptConfig(BaseModel):
    scriptRefName: str
    headers: Dict[str, str]

class RouteConfig(BaseModel):
    routeRefName: str
    dataDomain: str
    headers: Dict[str, str]

class Destination(BaseModel):
    scriptConfig: ScriptConfig
    routeConfig: RouteConfig

class SubscriptionDetails(BaseModel):
    id: Optional[PyObjectId] = Field(alias="_id")
    subscriptionId: str
    subscriptionType: str
    enabled: bool
    subscriptionUrl: str
    destinations: List[Destination]
    _class: Optional[str] = None

    class Config:
        allow_population_by_field_name = True
        arbitrary_types_allowed = True
