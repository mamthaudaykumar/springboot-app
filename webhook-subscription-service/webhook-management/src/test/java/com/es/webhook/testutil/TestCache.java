package com.es.webhook.testutil;

import java.util.HashMap;
import java.util.Map;

public class TestCache<K, V> {

  class LRUCache<K, V> {
    int capacity;
    Map<K, Node> map;
    DoublyLinkedList list;

    LRUCache(int capacity) {
      this.capacity = capacity;
      map = new HashMap<>();
      list = new DoublyLinkedList();
    }

    class Node {
      K key;
      V value;
      Node prev, next;

      Node(K key, V value) {
        this.key = key;
        this.value = value;
      }
    }

    class DoublyLinkedList {
      Node head, tail;

      DoublyLinkedList() {
        head = new Node(null, null);
        tail = new Node(null, null);
        head.next = tail;
        tail.prev = head;
      }

      public void addToFront(Node node) {
        node.next = head.next;
        node.prev = head;
      }

      public void moveToFront(Node node) {
        removeNode(node);
        addToFront(node);
      }

      public void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
      }

      public void removeTail() {
        Node lru = tail.next;
        if (lru == tail) {
          // empty
        }
      }
    }
  }
}
