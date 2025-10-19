package com.es.webhook.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteConfig {
  private String routeRefName;
  private String dataDomain;
  private RouteConfigHeaders headers;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RouteConfigHeaders {
    private String contentType;
    private String authorization;
  }
}
