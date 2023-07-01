package ru.clevertec.news.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ru.clevertec.news.annotation.MyCacheable;
import ru.clevertec.news.cache.Cache;
import ru.clevertec.news.cache.config.MyCacheConfig;

import java.lang.reflect.Field;

/**
 * Aspect class used for my own cache implementation
 *
 * @author Yuryeu Andrei
 */
@Aspect
@Component
@ConditionalOnBean(MyCacheConfig.class)
@RequiredArgsConstructor
public class CachingAspect {

    private final Cache cache;

    /**
     * FindById method of service pointcut
     */
    @Pointcut("execution(public !void ru.clevertec.news.service.*.findById(..))")
    public void findByIdServiceMethod() {
    }

    /**
     * Add and Update method of service pointcut
     */
    @Pointcut("execution(public !void ru.clevertec.news.service.*.add(..)) &&" +
            " execution(public !void ru.clevertec.news.service.*.update(..))")
    public void addAndUpdateServiceMethod() {
    }

    /**
     * Delete method of service pointcut
     */
    @Pointcut("execution(public !void ru.clevertec.news.service.*.delete(..))")
    public void deleteServiceMethod() {
    }

    /**
     * FindByID caching advice, puts the result of method invokation into the cache
     */
    @Around("findByIdServiceMethod()")
    public Object cachingFindByIdResults(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean hasAnnotation = proceedingJoinPoint.getTarget().getClass().isAnnotationPresent(MyCacheable.class);

        if (hasAnnotation) {
            Long id = (Long) proceedingJoinPoint.getArgs()[0];
            Object cachedObject = cache.get(id);
            if (cachedObject != null) {
                return cachedObject;
            } else {
                Object retVal = proceedingJoinPoint.proceed();
                cache.set(id, retVal);
                return retVal;
            }
        }
        return proceedingJoinPoint.proceed();
    }

    /**
     * Save and Update caching advice, puts the result of method invokation
     * into the cache(updates it in case of update)
     */
    @Around("addAndUpdateServiceMethod()")
    public Object cachingSaveAndUpdateResults(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean hasAnnotation = proceedingJoinPoint.getTarget().getClass().isAnnotationPresent(MyCacheable.class);

        if (hasAnnotation) {
            Object retVal = proceedingJoinPoint.proceed();
            Long id = findEntityId(retVal);
            cache.set(id, retVal);
        }

        return proceedingJoinPoint.proceed();
    }

    /**
     * Delete caching advice, removes element from cache
     */
    @Around("deleteServiceMethod()")
    public Object cachingDeleteResults(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean hasAnnotation = proceedingJoinPoint.getTarget().getClass().isAnnotationPresent(MyCacheable.class);

        if (hasAnnotation) {
            Long id = (Long) proceedingJoinPoint.getArgs()[0];
            if (cache.get(id) != null) {
                proceedingJoinPoint.proceed();
                cache.delete(id);
            }
        }

        return proceedingJoinPoint.proceed();
    }

    private Long findEntityId(Object object) throws Throwable {
        Long id = null;
        for (Field field : object.getClass().getDeclaredFields()) {
            if ("id".equals(field.getName())) {
                field.setAccessible(true);
                id = (Long) field.get(object);
                field.setAccessible(false);
            }
        }
        return id;
    }
}
