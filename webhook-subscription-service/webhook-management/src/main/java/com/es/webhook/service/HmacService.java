package com.es.webhook.service;

import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacService {

  public static String calculateHMAC(String data, String secret, String algorithm)
      throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(secretKey);
    byte[] hmacBytes = mac.doFinal(data.getBytes());
    return Base64.getEncoder().encodeToString(hmacBytes);
  }
}
