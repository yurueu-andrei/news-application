package ru.clevertec.handling.exception;

import org.springframework.http.HttpStatus;

/**
 * Abstract exception implementation(connected to registration process)
 *
 * @author Yuryeu Andrei
 * @see AbstractException
 */
public class RegisterException extends AbstractException {

    public RegisterException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
