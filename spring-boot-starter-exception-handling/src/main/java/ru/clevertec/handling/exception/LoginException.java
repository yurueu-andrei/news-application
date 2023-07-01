package ru.clevertec.handling.exception;

import org.springframework.http.HttpStatus;

/**
 * Abstract exception implementation(connected to logging in process)
 *
 * @author Yuryeu Andrei
 * @see AbstractException
 */
public class LoginException extends AbstractException {

    public LoginException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
