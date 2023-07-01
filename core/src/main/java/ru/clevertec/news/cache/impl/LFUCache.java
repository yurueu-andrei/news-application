package ru.clevertec.news.cache.impl;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.clevertec.news.cache.Cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LFU cache type implementation
 *
 * @author Yuryeu Andrei
 */
@EqualsAndHashCode
@ToString
public class LFUCache implements Cache {

    @ToString
    private static class Node {

        Object value;
        int useCount;
        long lastGetTime;

        public Node(Object value, int useCount) {
            this.value = value;
            this.useCount = useCount;
        }
    }

    private final int capacity;
    private final Map<Long, Node> cache;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
    }

    @Override
    public Object get(Long key) {
        if (!cache.containsKey(key)) {
            return null;
        }
        cache.get(key).useCount++;
        cache.get(key).lastGetTime = System.nanoTime();
        return cache.get(key).value;
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
        if (capacity == 0) return;
        if (cache.size() >= capacity) {
            removeMin();
        }
        Node node = new Node(value, 0);
        node.lastGetTime = System.nanoTime();
        cache.put(key, node);
    }

    @Override
    public void delete(Long key) {
        cache.remove(key);
    }

    private void removeMin() {
        int minCount = Integer.MAX_VALUE;
        long currTime = System.nanoTime();
        Long minKey = 0L;
        for (Long key : cache.keySet()) {
            Node node = cache.get(key);
            if (node.useCount < minCount || (node.useCount == minCount && node.lastGetTime < currTime)) {
                minKey = key;
                minCount = node.useCount;
                currTime = node.lastGetTime;
            }
        }
        cache.remove(minKey);
    }
}
