package ru.clevertec.handling.handler.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Class used for exception message transfer
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionMessage {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
