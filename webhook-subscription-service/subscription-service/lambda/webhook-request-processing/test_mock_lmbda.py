import unittest
from unittest.mock import patch, MagicMock
from lambda_handler import lambda_handler
import hmac
import hashlib


class TestLambdaHandler(unittest.TestCase):

    def setUp(self):
        self.secret = "testsecret"
        self.payload = '{"key": "value"}'
        self.algorithm = "HmacSHA256"
        self.signature = hmac.new(
            key=self.secret.encode(),
            msg=self.payload.encode(),
            digestmod=hashlib.sha256
        ).hexdigest()

        self.event = {
            "headers": {
                "X-Signature": self.signature,
                "SubscriptionId": "abc123"
            },
            "body": self.payload
        }

        self.context = {}  # Can be empty for test

    @patch("lambda_handler.get_subscription_config_details")
    def test_valid_signature(self, mock_get_subscription):
        # Create a mock subscription config object
        mock_auth_config = MagicMock()
        mock_auth_config.secretKey = self.secret
        mock_auth_config.algorithm = self.algorithm

        mock_auth = MagicMock()
        mock_auth.config = mock_auth_config

        mock_subscription = MagicMock()
        mock_subscription.authentication = mock_auth

        mock_get_subscription.return_value = mock_subscription

        response = lambda_handler(self.event, self.context)
        self.assertEqual(response["statusCode"], 200)
        self.assertIn("Signature verified", response["body"])

    @patch("lambda_handler.get_subscription_config_details")
    def test_invalid_signature(self, mock_get_subscription):
        self.event["headers"]["X-Signature"] = "invalidsignature"

        mock_auth_config = MagicMock()
        mock_auth_config.secretKey = self.secret
        mock_auth_config.algorithm = self.algorithm

        mock_auth = MagicMock()
        mock_auth.config = mock_auth_config

        mock_subscription = MagicMock()
        mock_subscription.authentication = mock_auth

        mock_get_subscription.return_value = mock_subscription

        response = lambda_handler(self.event, self.context)
        self.assertEqual(response["statusCode"], 403)

    @patch("lambda_handler.get_subscription_config_details")
    def test_missing_headers(self, mock_get_subscription):
        bad_event = {
            "headers": {},
            "body": self.payload
        }

        response = lambda_handler(bad_event, self.context)
        self.assertEqual(response["statusCode"], 400)

    @patch("lambda_handler.get_subscription_config_details")
    def test_subscription_not_found(self, mock_get_subscription):
        mock_get_subscription.return_value = None

        response = lambda_handler(self.event, self.context)
        self.assertEqual(response["statusCode"], 404)


if __name__ == "__main__":
    unittest.main()
