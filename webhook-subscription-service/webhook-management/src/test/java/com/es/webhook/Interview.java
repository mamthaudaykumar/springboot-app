package com.es.webhook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class Interview {

  @Test
  public void test() {
    List<Object> arr = Arrays.asList(1, 5, "abc", "xyz", 0.1, 20.0);

    Map<String, List<Object>> defTypeMap = new HashMap<>();
    //    group this list by its class type

    arr.stream()
        .forEach(
            e -> {
              if (e instanceof Integer) {
                defTypeMap
                    .computeIfAbsent(String.valueOf(Integer.class), k -> new ArrayList<>())
                    .add(e);
              } else if (e instanceof String) {
                defTypeMap
                    .computeIfAbsent(String.valueOf(String.class), k -> new ArrayList<>())
                    .add(e);
              } else if (e instanceof Double) {
                defTypeMap
                    .computeIfAbsent(String.valueOf(Double.class), k -> new ArrayList<>())
                    .add(e);
              }
            });
    System.out.println(defTypeMap);

    arr.stream().collect(Collectors.groupingBy(Object::toString, Collectors.toList()));

    //    arr.forEach(e -> {
    //      if(e instanceof Integer) {
    //
    //      } else if(e instanceof String)  {
    //
    //      } else if(e instanceof Double) {
    //
    //      }
    //    });
  }
}
