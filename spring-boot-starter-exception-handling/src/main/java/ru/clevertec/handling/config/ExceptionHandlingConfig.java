package ru.clevertec.handling.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.handling.handler.ExceptionSecurityFilter;
import ru.clevertec.handling.handler.GlobalExceptionHandler;

/**
 * Exception handling configuration config(creates handler and filter beans based on properties)
 *
 * @author Yuryeu Andrei
 */
@Configuration
@ConditionalOnProperty(
        prefix = "spring.exception-handler",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class ExceptionHandlingConfig {

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionSecurityFilter exceptionSecurityFilter(ObjectMapper mapper) {
        return new ExceptionSecurityFilter(mapper);
    }
}
