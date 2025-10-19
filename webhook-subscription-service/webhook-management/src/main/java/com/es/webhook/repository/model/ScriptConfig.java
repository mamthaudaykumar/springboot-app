package com.es.webhook.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScriptConfig {
  private String scriptRefName;
  private ScriptConfigHeaders headers;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ScriptConfigHeaders {
    private String contentType;
    private String authorization;
  }
}
