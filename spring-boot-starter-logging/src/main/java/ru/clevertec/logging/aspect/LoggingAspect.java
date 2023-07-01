package ru.clevertec.logging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.clevertec.logging.annotation.Logging;

import java.util.Arrays;

/**
 * Aspect class used for logging
 *
 * @author Yuryeu Andrei
 */
@Aspect
@Slf4j
public class LoggingAspect {

    /**
     * Method containing Logging annotation pointcut
     *
     * @see Logging
     */
    @Pointcut("@annotation(ru.clevertec.logging.annotation.Logging)")
    private void annotationPointcut() {
    }

    /**
     * Class containing Logging annotation pointcut
     *
     * @see Logging
     */
    @Pointcut("@within(ru.clevertec.logging.annotation.Logging)")
    private void typePointcut() {
    }

    /**
     * Logging the successful invokation of the method annotated with Logging annotation
     *
     * @see Logging
     */
    @AfterReturning(
            pointcut = "annotationPointcut() || typePointcut()",
            returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String message = "Result of executing method {}({}) in {} is {}";
        String method = joinPoint.getSignature().getName();
        String arguments = getArguments(joinPoint);
        String className = findClassName(joinPoint.getSignature().getDeclaringTypeName());
        String resultToString = result == null ? null : result.toString();

        log.info(message, method, arguments, className, resultToString);
    }

    /**
     * Logging the exceptional cases of the method annotated with Logging annotation
     *
     * @see Logging
     */
    @AfterThrowing(
            pointcut = "annotationPointcut() || typePointcut()",
            throwing = "exception"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        String message = "Exception in {} of {}({}) with cause = {}";
        String className = findClassName(joinPoint.getSignature().getDeclaringTypeName());
        String method = joinPoint.getSignature().getName();
        String arguments = getArguments(joinPoint);
        String exceptionMessage = exception.getMessage();

        log.error(message, className, method, arguments, exceptionMessage);
    }

    private String getArguments(JoinPoint joinPoint) {
        String arguments = Arrays.toString(joinPoint.getArgs());
        arguments = arguments.substring(1, arguments.length() - 1);
        return arguments;
    }

    private String findClassName(String pass) {
        return pass.substring(pass.lastIndexOf(".") + 1);
    }
}
