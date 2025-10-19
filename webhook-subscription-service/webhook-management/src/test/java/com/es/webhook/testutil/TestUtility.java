package com.es.webhook.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

public class TestUtility {

  @Test
  public void testRotate() {
    int k = 2;
    int[] arr = {2, 4, 5, 7, 6, 8};

    int length = arr.length;

    k = k % length;
    if (length != 0 || k % length != 0) {
      rotate(arr, 0, length - 1);
      rotate(arr, 0, k - 1);
      rotate(arr, k, length - 1);
    }

    // For verification
    System.out.println(Arrays.toString(arr)); // [6, 8, 2, 4, 5, 7]
  }

  public void rotate(int[] arr, int start, int end) {
    while (start < end) {
      int temp = arr[start];
      arr[start] = arr[end];
      arr[end] = temp;
      start++;
      end--;
    }
  }

  @Test
  public void stringAnagram() {
    //    ["cat", "bat", "tab","act","tac","tap"]
    //    bat,tab,cat,act,tac,tap
    //    abt=[bat,tab]
    //    act=[cat,act,tac]
    //    apt=[tap]

    List<String> arr = Arrays.asList("cat", "bat", "tab", "act", "tac", "tap");
    Map<String, List<String>> anagramMap = new HashMap<>();
    arr.stream()
        .forEach(
            e -> {
              char[] val = e.toCharArray();
              Arrays.sort(val);
              anagramMap
                  .computeIfAbsent(String.valueOf(val).toLowerCase(), k -> new ArrayList<>())
                  .add(e);
            });
    System.out.println(anagramMap);

    Map<String, List<String>> anagramMap2 =
        arr.stream()
            .collect(
                Collectors.groupingBy(
                    e -> {
                      char[] val = e.toCharArray();
                      Arrays.sort(val);
                      return String.valueOf(val).toLowerCase();
                    },
                    Collectors.toList()));
    System.out.println(anagramMap2);
  }

  @Test
  public void groupAnagram() {
    //   Find the Anagram in a string words are :- bat,tab,cat,act,tac,tap
    //   [bat,tab]
    //   [cat,act,tac]

    List<String> arr = Arrays.asList("cat", "bat", "tab", "act", "tac", "tap");
    //    List<List<String>> anagrams = new ArrayList<>();

    List<List<String>> anagrams =
        arr.stream()
            .collect(
                Collectors.groupingBy(
                    e -> {
                      char[] val = e.toCharArray();
                      Arrays.sort(val);
                      return String.valueOf(val).toLowerCase();
                    },
                    Collectors.toList()))
            .values()
            .stream()
            .collect(Collectors.toList());
    System.out.println(anagrams);
  }

  @Test
  public void startWordCount() {
    //    You have list of words .Group the words by their first letter and count how many words
    //    start with each letter.
    //    input : apple, mango, appricot, blueberry, banana
    //    output:-{a=2,b=2,m=1}

    List<String> arr = Arrays.asList("apple", "mango", "appricot", "blueberry", "banana");
    Map<Character, Integer> charCount = new HashMap<>();
    arr.forEach(
        e -> {
          Character c = e.charAt(0);
          charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        });
    System.out.println(charCount);

    Map<Object, Long> charCount2 =
        arr.stream()
            .collect(
                Collectors.groupingBy(
                    e -> {
                      return e.charAt(0);
                    },
                    Collectors.counting()));
    System.out.println(charCount2);
  }

  @Test
  public void employeeMaxSalary() {
    //    Group Employees by department and find max salary in each
    //    Employee class
    //    empName,empSalary,department

    List<Employee> employees =
        Arrays.asList(
            new Employee("John", 20, 5000, "IT"),
            new Employee("Alice", 20, 7000, "IT"),
            new Employee("Bob", 40, 6000, "HR"),
            new Employee("Carol", 35, 8000, "HR"));

    Map<String, List<Employee>> deptEmployees = new HashMap<>();
    for (Employee employee : employees) {
      deptEmployees
          .computeIfAbsent(employee.department, k -> new ArrayList<Employee>())
          .add(employee);
    }
    System.out.println(deptEmployees);

    Map<String, Employee> deptEmployeesMaxSalary = new HashMap<>();
    deptEmployees
        .entrySet()
        .forEach(
            e -> {
              Employee maxSal =
                  e.getValue().stream().max(Comparator.comparing(Employee::getEmpSalary)).get();
              deptEmployeesMaxSalary.put(e.getKey(), maxSal);
            });
    System.out.println(deptEmployeesMaxSalary);

    Map<String, Employee> deptEmployees2 =
        employees.stream()
            .collect(
                Collectors.groupingBy(
                    Employee::getDepartment,
                    Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparing(Employee::getEmpSalary)),
                        Optional::get)));
    System.out.println(deptEmployees2);
    //        .entrySet().stream().collect(Entry::getKey, )
  }

  @Test
  public void wordCount() {
    //    /** Implement here to find word occurrence in the lines **/return 0;}

    List<String> lines =
        Arrays.asList("Apple is a fruit.", "I like apple pie.", "APPLE cider is tasty.");

    int wordCount = 0;
    for (String word : lines) {
      String[] words = word.toLowerCase().split(" ");
      for (String w : words) {
        if (w.equals("apple")) {
          wordCount++;
        }
      }
    }
    System.out.println(wordCount);
  }

  @Data
  @AllArgsConstructor
  public class Employee {
    private String empName;
    private int age;
    private int empSalary;
    private String department;
  }
}
