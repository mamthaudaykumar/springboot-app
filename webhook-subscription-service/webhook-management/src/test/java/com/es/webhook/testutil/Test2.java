package com.es.webhook.testutil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

public class Test2 {

  @Test
  public void test() {
    LinkedList<Integer> l1 = new LinkedList<>();
    l1.addAll(Arrays.asList(2, 4, 3));
    LinkedList<Integer> l2 = new LinkedList<>();
    l2.addAll(Arrays.asList(5, 6, 4)); // ✅ fixed

    // Build the number as a string (in reverse order!)
    StringBuilder sb1 = new StringBuilder();
    l1.descendingIterator().forEachRemaining(e -> sb1.append(e));
    StringBuilder sb2 = new StringBuilder();
    l2.descendingIterator().forEachRemaining(e -> sb2.append(e));

    // Convert to BigInteger
    BigInteger num1 = new BigInteger(sb1.toString());
    BigInteger num2 = new BigInteger(sb2.toString());

    // Add
    BigInteger sum = num1.add(num2);
    System.out.println(sum);

    // Convert back to reversed LinkedList<Integer>
    LinkedList<Integer> result = new LinkedList<>();
    String sumStr = new StringBuilder(sum.toString()).reverse().toString();
    for (char c : sumStr.toCharArray()) {
      result.add(Character.getNumericValue(c));
    }

    System.out.println("Result list: " + result);
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @RequiredArgsConstructor(staticName = "val")
  public class ListNode {
    int val;
    ListNode next;
  }

  public void addTwoNumbers(ListNode l1, ListNode l2) {

    //    Input: l1 = [2,4,3], l2 = [5,6,4]
    //    Output: [7,0,8]
    //    Explanation: 342 + 465 = 807.
    //    Example 2:
    //
    //    Input: l1 = [0], l2 = [0]
    //    Output: [0]
    //    Example 3:
    //
    //    Input: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
    //    Output: [8,9,9,9,0,0,0,1]

    //  }
    //
    //  public void nodeValuesSum() {
    //    ListNode l1 = new ListNode()
    //  }
  }
}
