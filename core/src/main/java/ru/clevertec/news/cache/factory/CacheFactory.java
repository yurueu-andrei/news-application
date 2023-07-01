package ru.clevertec.news.cache.factory;

import ru.clevertec.news.cache.Cache;
import ru.clevertec.news.cache.impl.LFUCache;
import ru.clevertec.news.cache.impl.LRUCache;

/**
 * Class used for exception message transfer
 *
 * @author Yuryeu Andrei
 */
public class CacheFactory {

    /**
     * Factory-method used for cache creating
     */
    public Cache createCache(String cacheType, int cacheSize) {
        return switch (cacheType) {
            case "LFU" -> new LFUCache(cacheSize);
            case "LRU" -> new LRUCache(cacheSize);
            default -> null;
        };
    }
}
