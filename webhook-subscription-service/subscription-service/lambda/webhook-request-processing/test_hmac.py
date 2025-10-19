import unittest
from services.hmac import verify_hmac
import hashlib
import hmac


class TestHMACVerification(unittest.TestCase):
    def setUp(self):
        self.secret = "testsecret"
        self.payload = '{"sample": "payload"}'
        self.algorithms = {
            "HmacSHA256": hashlib.sha256,
            "HmacSHA384": hashlib.sha384,
            "HmacSHA512": hashlib.sha512
        }

    def generate_signature(self, algorithm):
        return hmac.new(
            key=self.secret.encode(),
            msg=self.payload.encode(),
            digestmod=self.algorithms[algorithm]
        ).hexdigest()

    def test_valid_hmac_sha256(self):
        signature = self.generate_signature("HmacSHA256")
        self.assertTrue(verify_hmac(self.secret, "HmacSHA256", self.payload, signature))

    def test_valid_hmac_sha384(self):
        signature = self.generate_signature("HmacSHA384")
        self.assertTrue(verify_hmac(self.secret, "HmacSHA384", self.payload, signature))

    def test_valid_hmac_sha512(self):
        signature = self.generate_signature("HmacSHA512")
        self.assertTrue(verify_hmac(self.secret, "HmacSHA512", self.payload, signature))

    def test_invalid_signature(self):
        wrong_signature = "deadbeef"
        self.assertFalse(verify_hmac(self.secret, "HmacSHA256", self.payload, wrong_signature))

    def test_unsupported_algorithm(self):
        self.assertFalse(verify_hmac(self.secret, "UnknownAlgo", self.payload, "whatever"))


if __name__ == "__main__":
    unittest.main()
