package ru.clevertec.handling.exception;

import org.springframework.http.HttpStatus;

/**
 * Abstract exception implementation(connected to service classes activities)
 *
 * @author Yuryeu Andrei
 * @see AbstractException
 */
public class ServiceException extends AbstractException {

    public ServiceException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
