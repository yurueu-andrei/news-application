package ru.clevertec.news.annotation.conditional.condition;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import static org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION;

public class OnCorrectCacheProperties extends AnyNestedCondition {

    public OnCorrectCacheProperties() {
        super(PARSE_CONFIGURATION);
    }

    @ConditionalOnProperty(prefix = "cache", name = "algorithm", havingValue = "LRU")
    static class ConditionForLruCache {

    }

    @ConditionalOnProperty(prefix = "cache", name = "algorithm", havingValue = "LFU")
    static class ConditionForLfuCache {

    }
}
