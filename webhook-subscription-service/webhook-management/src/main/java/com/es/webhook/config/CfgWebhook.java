package com.es.webhook.config;

import com.es.webhook.aws.sqs.AwsClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableMongoAuditing
@Configuration
public class CfgWebhook {

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/v3/api-docs").allowedOrigins("*");
      }
    };
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new io.swagger.v3.oas.models.info.Info()
                .title("My API")
                .version("1.0")
                .description("This is the API documentation for my Spring Boot application")
                .license(
                    new io.swagger.v3.oas.models.info.License()
                        .name("MIT")
                        .url("https://opensource.org/licenses/MIT")));
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public CfgAppProperties cfgAppProperties() {
    return new CfgAppProperties();
  }

  @Bean
  public AwsClient awsClient(CfgAppProperties cfgAppProperties) {
    return new AwsClient(cfgAppProperties);
  }
}
