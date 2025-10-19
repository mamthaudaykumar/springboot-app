import unittest
import json
import hmac
import hashlib

from lambda_handler import lambda_handler
from services.subscription_config_details_dao import get_subscription_config_details


class TestLambdaHandlerE2E(unittest.TestCase):

    def setUp(self):
        self.subscription_id = "abc123"  # Must exist in your DB
        self.platform_name = "salesforce"
        self.subscription_type = "orderCreated"

        self.payload_dict = {"event": "order created", "orderId": 789}
        self.payload_str = json.dumps(self.payload_dict)

        # Get expected config from the real DAO (E2E, no mocks)
        self.subscription_config = get_subscription_config_details(self.subscription_id)
        self.auth_config = self.subscription_config.authentication.config

        self.secret = self.auth_config["secretKey"]
        self.algorithm = self.auth_config["algorithm"]
        self.header_name = self.auth_config["headerName"]

        self.signature = self._generate_signature(self.payload_str)

    def _generate_signature(self, payload):
        algo = self.algorithm.lower()
        if algo == "sha256":
            digestmod = hashlib.sha256
        elif algo == "sha1":
            digestmod = hashlib.sha1
        else:
            raise ValueError(f"Unsupported algorithm: {self.algorithm}")

        h = hmac.new(self.secret.encode(), payload.encode(), digestmod)
        return h.hexdigest()

    def test_valid_signature(self):
        event = {
            "httpMethod": "POST",
            "path": f"/api/v1/webhook/subscription/{self.subscription_id}/platform/{self.platform_name}/{self.subscription_type}",
            "headers": {
                self.header_name: self.signature
            },
            "body": self.payload_str
        }

        result = lambda_handler(event, context={})

        self.assertEqual(result["statusCode"], 200)
        self.assertEqual(result["body"], "Signature verified")

    def test_invalid_signature(self):
        event = {
            "httpMethod": "POST",
            "path": f"/api/v1/webhook/subscription/{self.subscription_id}/platform/{self.platform_name}/{self.subscription_type}",
            "headers": {
                self.header_name: "invalid_signature"
            },
            "body": self.payload_str
        }

        result = lambda_handler(event, context={})

        self.assertEqual(result["statusCode"], 403)
        self.assertEqual(result["body"], "Invalid signature")


if __name__ == "__main__":
    unittest.main()
