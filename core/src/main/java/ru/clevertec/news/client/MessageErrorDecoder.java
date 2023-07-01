package ru.clevertec.news.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.clevertec.handling.exception.LoginException;
import ru.clevertec.handling.handler.message.ExceptionMessage;

import java.io.InputStream;

/**
 * Open Feign error decoder class
 *
 * @author Yuryeu Andrei
 */
@Component
@RequiredArgsConstructor
public class MessageErrorDecoder implements ErrorDecoder {

    private final ObjectMapper mapper;

    /**
     * Translates every exception connected with feign clients into LoginException
     */
    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            ExceptionMessage message = mapper.readValue(bodyIs, ExceptionMessage.class);
            return new LoginException(message.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
