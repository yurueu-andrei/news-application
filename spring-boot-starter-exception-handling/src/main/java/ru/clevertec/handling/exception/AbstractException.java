package ru.clevertec.handling.exception;

import org.springframework.http.HttpStatus;

/**
 * Abstract exception(runtime exception which contains http status)
 *
 * @author Yuryeu Andrei
 */
public abstract class AbstractException extends RuntimeException {

    private final HttpStatus httpStatus;

    public AbstractException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
