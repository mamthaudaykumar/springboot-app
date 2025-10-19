package com.es.webhook.testutil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;

public class Test {

  @org.junit.jupiter.api.Test
  public void rotateTest() {
    int[] arr1 = {2, 4, 5, 7, 6, 8};
    int k = 2;
    rotate(arr1, k);
    System.out.println(Arrays.toString(arr1)); // [6, 8, 2, 4, 5, 7]

    int[] arr2 = {2, 4, 5, 7, 6, 8};
    rotate(arr2, 4);
    System.out.println(Arrays.toString(arr2)); // [5, 7, 6, 8, 2, 4]
  }

  public void rotate2(int[] arr, int k) {
    int length = arr.length;
    if (length == 0 || k % length == 0) return;
    int rotateTimes = k % length;
    //    reverse2(arr, 0, length - 1);
    reverse2(arr, 0, k - 1);
    //    reverse2(arr, 0, length - k);
  }

  public void reverse2(int[] arr, int start, int end) {
    while (start <= end) {
      int temp = arr[start];
      arr[start] = arr[end];
      arr[end] = temp;
      start++;
      end--;
    }
  }

  @org.junit.jupiter.api.Test
  public void testRotate() {
    int[] arr1 = {2, 4, 5, 7, 6, 8};
    rotate(arr1, 2);
    System.out.println(Arrays.toString(arr1)); // [6, 8, 2, 4, 5, 7]

    int[] arr2 = {2, 4, 5, 7, 6, 8};
    rotate(arr2, 4);
    System.out.println(Arrays.toString(arr2)); // [5, 7, 6, 8, 2, 4]
  }

  // Rotate array clockwise by k steps
  public void rotate(int[] arr, int k) {
    int n = arr.length;
    if (n == 0 || k % n == 0) return;

    k = k % n; // handle k > n

    // Step 1: reverse entire array
    reverse(arr, 0, n - 1);

    // Step 2: reverse first k elements
    reverse(arr, 0, k - 1);

    // Step 3: reverse remaining n-k elements
    reverse(arr, k, n - 1);
  }

  // Utility: reverse subarray in-place
  private void reverse(int[] arr, int start, int end) {
    while (start < end) {
      int temp = arr[start];
      arr[start] = arr[end];
      arr[end] = temp;
      start++;
      end--;
    }
  }

  @org.junit.jupiter.api.Test
  public void stringAnalysis() {

    //    ["cat", "bat", "tab","act","tac","tap"]
    //    bat,tab,cat,act,tac,tap
    //    abt=[bat,tab]
    //    act=[cat,act,tac]
    //    apt=[tap]

    List<String> words = Arrays.asList("cat", "bat", "tab", "act", "tac", "tap");
    Map<String, List<String>> anagrams = new HashMap<>();
    words.forEach(
        e -> {
          char[] letters = e.toCharArray();
          Arrays.sort(letters);
          String key = new String(letters); // <-- FIXED
          anagrams.computeIfAbsent(key, k -> new ArrayList<>()).add(e);
        });
    anagrams
        .entrySet()
        .forEach(
            e -> {
              System.out.println(e.getKey() + " : " + e.getValue());
            });
  }

  @org.junit.jupiter.api.Test
  public void letterCount() {
    //    You have list of words .Group the words by their first letter and count how many words
    //    start with each letter.
    //    input : apple, mango, appricot, blueberry,banana
    //    output:{a=2,b=2,m=1}

    List<String> words = Arrays.asList("apple", "mango", "appricot", "blueberry", "banana");
    Map<Character, Integer> startCharWordCount = new HashMap<>();
    words.forEach(
        e -> {
          char letters = e.charAt(0);
          startCharWordCount.put(letters, startCharWordCount.getOrDefault(letters, 0) + 1);
        });

    System.out.println(startCharWordCount);
  }

  @org.junit.jupiter.api.Test
  public void wordOccurances() {

    // Input lines
    List<String> lines =
        Arrays.asList("Apple is a fruit.", "I like apple pie.", "APPLE cider is tasty.");

    String targetWord = "apple";

    //    long count =
    //        lines.stream()
    //            .flatMap(line -> Arrays.stream(line.split("\\W+"))) // split into words
    //            .map(String::toLowerCase) // normalize case
    //            .filter(word -> word.equals(targetWord)) // match only "apple"
    //            .count();

    long count =
        lines.stream()
            .flatMap(e -> Arrays.stream(e.split(" ")))
            .map(String::toLowerCase)
            .filter(word -> word.equals(targetWord))
            .count();
    System.out.println("Occurrences of '" + targetWord + "': " + count);

    List<String> lines2 =
        Arrays.asList(
            "Apple is a fruit and apple juice is healthy.",
            "I like apple pie, apple crumble, and apple tart.",
            "APPLE cider is tasty, and APPLE jam is sweet.");

    long lines2Count =
        lines2.stream()
            .flatMap(e -> Arrays.stream(e.split(" ")))
            .map(String::toLowerCase)
            .filter(f -> f.equals(targetWord))
            .count();
    System.out.println("Occurrences of '" + targetWord + "': " + lines2Count);
  }

  @org.junit.jupiter.api.Test
  public void matchToCorrectInstance() {
    //    Given List<Object> = [1,5,"abc", "xyz", 0.1,20.0];
    //    group this list by its class type sort the map by its value.
    //    like map keys should be in sorted order,
    //    [Double, Integer, String] sort the map by its value . eg .
    //  for Integer - list <1,2,3> group the list by its parent class only for numbers.
    //        like instead of grouping integer into integer , float into float, group all
    // integer,float,
    //    double values into Number class (parent)

    //        Number -> [0.1, 1, 5, 20.0]
    //    String -> [abc, xyz]

    Map<String, List<Object>> valuesTypeMap = new HashMap<>();
    List<Object> values = Arrays.asList(1, 5, "xyx", "abc", "xyz", 0.1, 20.0);
    values.forEach(
        e -> {
          if (e instanceof Number) {
            valuesTypeMap.computeIfAbsent("Number", k -> new ArrayList<>()).add(e);
          } else if (e instanceof String) {
            valuesTypeMap.computeIfAbsent("String", k -> new ArrayList<>()).add(e);
          }
        });
    System.out.println(valuesTypeMap);

    valuesTypeMap
        .entrySet()
        .forEach(
            e -> {
              if (e.getValue().equals("")) {
                e.getValue().sort((a, b) -> Double.compare((Double) a, (Double) b));
              }
            });

    valuesTypeMap
        .entrySet()
        .forEach(
            e -> {
              if (e.getKey() == "Number") {
                e.getValue()
                    .sort(
                        (a, b) ->
                            Double.compare(((Number) a).doubleValue(), ((Number) b).doubleValue()));
              } else {
                e.getValue().sort((a, b) -> ((String) a).compareTo((String) b));
              }
            });

    System.out.println(valuesTypeMap);
  }

  @org.junit.jupiter.api.Test
  public void testFileCharCount() throws FileNotFoundException {
    String filePath = "";
    int charCount = 0;
    try (FileReader reader = new FileReader(filePath)) {
      int ch;
      while ((ch = reader.read()) != -1) {
        charCount++; // count each character
      }
      System.out.println("Total characters in file: " + charCount);
    } catch (IOException e) {
      e.printStackTrace();
    }

    try (FileReader reader = new FileReader(filePath)) {
      int ch;
      while ((ch = reader.read()) != -1) {
        charCount++;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void query() {
    //    Group Employees by department and find max salary in each
    //    Employee class
    //    empName,empSalary,department

    //    SELECT max(empSalary) , department from EMployee group by department

    List<Employee> employees =
        Arrays.asList(
            new Employee("John", 20, 5000, "IT"),
            new Employee("Alice", 20, 7000, "IT"),
            new Employee("Bob", 40, 6000, "HR"),
            new Employee("Carol", 35, 8000, "HR"));

    Map<String, Optional<Employee>> maxByDept =
        employees.stream()
            .collect(
                Collectors.groupingBy(
                    Employee::getDepartment,
                    Collectors.maxBy(Comparator.comparingDouble(Employee::getEmpSalary))));

    employees.stream()
        .collect(
            Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.maxBy(Comparator.comparingDouble(Employee::getEmpSalary))));

    maxByDept.forEach((dept, emp) -> System.out.println(dept + " -> " + emp.get().getEmpSalary()));

    employees.stream()
        .collect(
            Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.maxBy(Comparator.comparingDouble(Employee::getEmpSalary))));
  }

  @AllArgsConstructor
  @Data
  class Employee {
    String empName;
    int age;
    double empSalary;
    String department;

    // constructor, getters, setters
  }

  @org.junit.jupiter.api.Test
  public void noRepeatingCharacter() {
    //    find the first non repeating character "swiss" .
    String word = "swiss".toString().toLowerCase();
    char[] wordChar = word.toCharArray();
    Map<Character, Integer> charCount = new HashMap<>();
    for (char c : wordChar) {
      charCount.put(c, charCount.getOrDefault(c, 0) + 1);
    }

    for (Entry val : charCount.entrySet()) {
      if (val.getValue().equals(1)) {
        System.out.println(val.getKey());
        return;
      }
    }
  }

  @org.junit.jupiter.api.Test
  public void testStreamWorkFrequescy() {

    List<String> words = List.of("apple", "banana", "apple", "orange", "banana", "banana");
    Map<String, Long> wordCount =
        words.stream().collect(Collectors.groupingBy(String::toLowerCase, Collectors.counting()));
    System.out.println(wordCount);
  }

  @org.junit.jupiter.api.Test
  public void streamNonRepeatingCharacters() {
    //    3. Find First Non-Repeating Character
    //    Solution:
    String input = "swiss";
    Map<Character, Long> charCount =
        input
            .chars()
            .mapToObj(e -> (char) e)
            .collect(Collectors.groupingBy(c -> c, HashMap::new, Collectors.counting()));
    System.out.println(charCount);

    Character ch =
        input
            .chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(e -> e, HashMap::new, Collectors.counting()))
            .entrySet()
            .stream()
            .filter(e -> e.getValue() == 1)
            .findFirst()
            .map(Entry::getKey)
            .orElse(null);
  }

  @org.junit.jupiter.api.Test
  public void findDuplicatesFromListUsingStream() {
    //    Find Duplicate Elements in a List
    List<String> words = List.of("apple", "banana", "apple", "orange", "banana", "banana");
    List<String> duplicates =
        words.stream()
            .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
            .entrySet()
            .stream()
            .filter(e -> e.getValue() > 1)
            .map(Entry::getKey)
            .collect(Collectors.toList());
    System.out.println(duplicates);
  }

  @org.junit.jupiter.api.Test
  public void findAnagrams() {
    //    5. Find All Anagrams in a List of Strings
    //    Solution:-
    List<String> anagramList = List.of("bat", "tab", "cat", "act", "tac", "tap");

    Map<String, List<String>> anagrams =
        anagramList.stream()
            .collect(
                Collectors.groupingBy(
                    word -> {
                      char[] a = word.toCharArray();
                      Arrays.sort(a);
                      return new String(a);
                    }));

    System.out.println(anagrams);
  }

  @org.junit.jupiter.api.Test
  public void findKthSmallestElement() {
    //    6. Find the kth Smallest Element in an Array
    //    Solution:-
    int k = 5;
    int[] array = {4, 2, 7, 1, 5, 3, 6};
    int n = Arrays.stream(array).sorted().skip(k - 1).findFirst().orElse(-1);
    System.out.println(n);
  }

  @org.junit.jupiter.api.Test
  public void employeeDetailAnalysisUsingStream() {
    // Group Employees by Department into Sorted Map
    List<Employee> employees =
        Arrays.asList(
            new Employee("John", 35, 9000, "IT"),
            new Employee("Alice", 45, 7000, "IT"),
            new Employee("Bob", 35, 10000, "HR"),
            new Employee("Carol", 50, 8000, "HR"));

    Map<String, List<Employee>> deptEmpDetails =
        employees.stream()
            .collect(
                Collectors.groupingBy(Employee::getDepartment, TreeMap::new, Collectors.toList()));
    System.out.println(deptEmpDetails);

    deptEmpDetails.entrySet().stream()
        .forEach(
            e -> {
              e.getValue().sort(Comparator.comparing(Employee::getEmpSalary));
            });

    System.out.println(deptEmpDetails);
  }

  @org.junit.jupiter.api.Test
  public void mergeTwoList() {
    //    8. Merge Two Unsorted Arrays into Single Sorted Array Without Duplicates
    int[] a = new int[] {4, 2, 5, 1};
    int[] b = new int[] {8, 1, 9, 5};

    Set<Integer> nums = Arrays.stream(a).mapToObj(e -> (Integer) e).collect(Collectors.toSet());
    nums.addAll(Arrays.stream(b).mapToObj(e -> (Integer) e).collect(Collectors.toSet()));
    nums.stream().sorted();
    System.out.println(nums);

    int[] c = IntStream.concat(Arrays.stream(a), Arrays.stream(b)).sorted().distinct().toArray();
    System.out.println(Arrays.toString(c));
  }

  @org.junit.jupiter.api.Test
  public void sumNumbers() {

    //    9. Find Sum of All Digits of a Number
    int i = 15623;
    int sum = String.valueOf(i).chars().map(e -> e - '0').sum();
    System.out.println(sum);
  }

  @org.junit.jupiter.api.Test
  public void fibonacciStream() {
    //    10. Fibonacci Series Using Stream

    //    Stream.iterate(new int[] {0, 1}, t -> new int[] {t[1], t[0] + t[1]})
    //        .limit(10)
    //        .map(t -> t[0])
    //        .forEach(System.out::println);

    int i = 0;
    int previous = 0;
    int current = 1;
    System.out.println(current);
    while (i <= 10) {
      int num = previous + current;
      System.out.println(num);
      previous = current;
      current = num;
      i++;
    }

    Stream.iterate(new int[] {0, 1}, n -> new int[] {n[1], n[0] + n[1]})
        .limit(10)
        .map(t -> t[0])
        .forEach(System.out::println);
  }

  @org.junit.jupiter.api.Test
  public void seperateOddEvenNumbers() {
    //    11. Separate Odd and Even Numbers

    List<Integer> listOfIntegers = Arrays.asList(71, 18, 42, 21, 67, 32, 95, 14, 56, 87);

    Map<String, List<Integer>> oddEvenNumbersMap = new HashMap<>();
    oddEvenNumbersMap =
        listOfIntegers.stream().collect(Collectors.groupingBy(i -> i % 2 == 0 ? "EVEN" : "ODD"));
    System.out.println(oddEvenNumbersMap);
  }

  @org.junit.jupiter.api.Test
  public void maxSalaryEmployee() {
    //    12. Find the Maximum Salary Employee
    //    Solution:-
    List<Employee> employees =
        List.of(
            new Employee("Alice", 30, 4000, "HR"),
            new Employee("Bob", 45, 7000, "IT"),
            new Employee("Charlie", 50, 7500, "HR"),
            new Employee("David", 25, 5000, "IT"));
    String empName =
        employees.stream()
            .max(Comparator.comparing(Employee::getEmpSalary))
            .map(Employee::getEmpName)
            .orElse(null);
    System.out.println(empName);
  }

  @Data
  @AllArgsConstructor
  class Product {
    private String name;
    private double price;
    // constructor, getters
  }

  @Data
  @AllArgsConstructor
  class Order {
    private List<Product> products;
    // constructor, getter
  }

  @org.junit.jupiter.api.Test
  public void extractProductNamesByCondition() {
    //    3. Flatten Orders and Extract Product Names > $100

    List<Order> orders =
        List.of(
            new Order(List.of(new Product("Laptop", 1500), new Product("Mouse", 30))),
            new Order(List.of(new Product("Phone", 900), new Product("Charger", 25))),
            new Order(List.of(new Product("TV", 1200), new Product("Headphones", 150))));

    List<String> products =
        orders.stream()
            .flatMap(e -> e.getProducts().stream())
            .filter(e -> e.getPrice() > 100)
            .map(Product::getName)
            .collect(Collectors.toList());
    System.out.println(products);
  }

  @org.junit.jupiter.api.Test
  public void firstLetterWords() {
    //    14 You have a list of words. Group the words by their first letter and count how
    //    many words start with each letter.
    //    Solution:-
    List<String> words = List.of("apple", "banana", "apricot", "blueberry", "cherry", "apple");
    Map<Object, Long> wordCounts =
        words.stream().collect(Collectors.groupingBy(word -> word, Collectors.counting()));
    System.out.println(wordCounts);
  }

  @Data
  class ProductNew {
    String category;
    String name;
    double price;

    ProductNew(String category, String name, double price) {
      this.category = category;
      this.name = name;
      this.price = price;
    }
  }

  @org.junit.jupiter.api.Test
  public void product() {

    //    15. Given a list of Product objects, each having a category, name,
    //        and price, you need to:
    // • Group products by their category.
    // • Within each category, sort by price in ascending order
    // • For products with the same price, sort by name alphabetically.

    List<ProductNew> products =
        Arrays.asList(
            new ProductNew("Electronics", "Laptop", 1500),
            new ProductNew("Electronics", "Phone", 800),
            new ProductNew("Clothing", "Shirt", 25),
            new ProductNew("Clothing", "Pants", 30),
            new ProductNew("Clothing", "T-shirt", 20));

    Map<String, List<ProductNew>> depPRodData =
        products.stream()
            .collect(
                Collectors.groupingBy(
                    ProductNew::getCategory,
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        e ->
                            e.stream()
                                .sorted(
                                    Comparator.comparing(ProductNew::getPrice)
                                        .thenComparing(ProductNew::getName))
                                .toList())));
    System.out.println(depPRodData);
  }
}
