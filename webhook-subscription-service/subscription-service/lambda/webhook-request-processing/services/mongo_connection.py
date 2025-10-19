from pymongo import MongoClient
import os
import logging

# Logging setup
logger = logging.getLogger()
logger.setLevel(logging.INFO)

# MongoDB configuration from environment variables
MONGO_URI = os.environ.get("MONGO_URI")
DB_NAME = os.environ.get("MONGO_DB")

_client = None

def mongo_connection():
    global _client
    if _client is None:
        try:
            _client = MongoClient(MONGO_URI)
            logger.info("MongoDB client initialized.")
        except Exception as e:
            logger.error(f"Failed to connect to MongoDB: {e}")
            raise e
    return _client[DB_NAME]
