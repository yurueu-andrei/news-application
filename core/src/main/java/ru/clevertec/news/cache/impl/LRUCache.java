package ru.clevertec.news.cache.impl;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.clevertec.news.cache.Cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LRU cache type implementation
 *
 * @author Yuryeu Andrei
 */
@EqualsAndHashCode
@ToString
public class LRUCache implements Cache {

    @ToString
    private static class Node {

        @ToString.Exclude
        Node prev;
        @ToString.Exclude
        Node next;
        Long key;
        Object value;

        public Node(Long key, Object value) {
            this.key = key;
            this.value = value;
            prev = next = null;
        }
    }

    private final int capacity;
    private final Map<Long, Node> cache;
    @ToString.Exclude
    private final Node head;
    @ToString.Exclude
    private final Node tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        head = new Node(null, null);
        tail = new Node(null, null);
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public Object get(Long key) {
        if (cache.containsKey(key)) {
            Node cur = cache.get(key);
            cur.prev.next = cur.next;
            cur.next.prev = cur.prev;
            cur.prev = null;
            cur.next = null;
            moveToTail(cur);
            return cache.get(key).value;
        } else {
            return null;
        }
    }

    @Override
    public List<Object> getAll() {
        return cache.keySet().stream().map(this::get).toList();
    }

    @Override
    public void set(Long key, Object value) {
        if (get(key) != null) {
            cache.get(key).value = value;
            return;
        }
        if (cache.size() == capacity) {
            cache.remove(head.next.key);
            head.next = head.next.next;
            head.next.prev = head;
        }
        Node newNode = new Node(key, value);
        cache.put(key, newNode);
        moveToTail(newNode);
    }

    @Override
    public void delete(Long key) {
        cache.remove(key);
    }

    private void moveToTail(Node cur) {
        tail.prev.next = cur;
        cur.prev = tail.prev;
        cur.next = tail;
        tail.prev = cur;
    }
}
