package ru.clevertec.handling.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.handling.exception.AbstractException;
import ru.clevertec.handling.handler.message.ExceptionMessage;

import java.time.LocalDateTime;

/**
 * Global exception handler
 *
 * @author Yuryeu Andrei
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Abstract exception handler
     *
     * @see AbstractException
     */
    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<ExceptionMessage> handleServiceAndAuthenticationException(
            HttpServletRequest request,
            AbstractException ex
    ) {
        ExceptionMessage response = new ExceptionMessage(
                LocalDateTime.now(),
                ex.getHttpStatus().value(),
                ex.getHttpStatus().getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    /**
     * Validation exception handler
     *
     * @see MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessage> handleValidationException(
            HttpServletRequest request,
            Exception ex
    ) {
        ExceptionMessage response = new ExceptionMessage(
                LocalDateTime.now(),
                400,
                "Bad Request",
                "Arguments are not valid",
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Other exceptions handler(returns 400 status code)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleOtherExceptions(
            HttpServletRequest request,
            Exception ex
    ) {
        ExceptionMessage response = new ExceptionMessage(
                LocalDateTime.now(),
                400,
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
