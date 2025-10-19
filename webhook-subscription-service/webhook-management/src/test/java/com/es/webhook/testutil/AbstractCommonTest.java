package com.es.webhook.testutil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@TestPropertySource(properties = {"spring.application.name=testutil"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestExecutionListeners(
    value = {DependencyInjectionTestExecutionListener.class},
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public abstract class AbstractCommonTest {
  private static final Logger logger = LoggerFactory.getLogger(AbstractCommonTest.class);

  @BeforeEach
  public void setUp() {
    logger.debug("Setting up the test");
  }

  @AfterEach
  public void tearDown() {
    logger.debug("Tearing down the test");
  }

  public InputStream readFile(String file) {
    return getClass().getClassLoader().getResourceAsStream(file);
  }

  public String readJsonFromFile(String file) throws Exception {
    String jsonString = null;

    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file)) {
      if (inputStream == null) {
        throw new RuntimeException("File not found: " + file);
      }

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(inputStream);
      jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

    } catch (Exception e) {
      logger.error("Error while reading JSON from file", e);
      throw e;
    }

    return jsonString; // Return the JSON string
  }
}
