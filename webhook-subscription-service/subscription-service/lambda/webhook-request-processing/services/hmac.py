import hashlib
import hmac
import logging

logger = logging.getLogger()
logger.setLevel(logging.INFO)


def get_algorithm(algorithm_val):
    algo_map = {
        "HmacSHA256": hashlib.sha256,
        "HmacSHA384": hashlib.sha384,
        "HmacSHA512": hashlib.sha512,
    }
    return algo_map.get(algorithm_val)


def verify_hmac(secret, algorithm_val, payload, received_signature):
    if isinstance(payload, str):
        payload = payload.encode("utf-8")

    digestmod = get_algorithm(algorithm_val)
    if not digestmod:
        logger.error(f"Unsupported algorithm: {algorithm_val}")
        return False

    expected_signature = hmac.new(
        key=secret.encode(),
        msg=payload,
        digestmod=digestmod
    ).hexdigest()

    return hmac.compare_digest(expected_signature, received_signature)
