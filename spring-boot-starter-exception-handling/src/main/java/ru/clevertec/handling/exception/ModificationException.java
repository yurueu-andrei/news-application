package ru.clevertec.handling.exception;

import org.springframework.http.HttpStatus;

/**
 * Abstract exception implementation(connected to modifying operations)
 *
 * @author Yuryeu Andrei
 * @see AbstractException
 */
public class ModificationException extends AbstractException {

    public ModificationException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
