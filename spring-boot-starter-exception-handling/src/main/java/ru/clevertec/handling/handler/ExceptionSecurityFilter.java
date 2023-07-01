package ru.clevertec.handling.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.handling.handler.message.ExceptionMessage;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Exception handling spring security filter
 *
 * @author Yuryeu Andrei
 */
@RequiredArgsConstructor
public class ExceptionSecurityFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException ex) {
            handleException(request, response, ex);
        }
    }

    @SneakyThrows
    public void handleException(
            HttpServletRequest request,
            HttpServletResponse response,
            RuntimeException ex) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        ExceptionMessage message = new ExceptionMessage(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        response.getWriter().write(mapper.writeValueAsString(message));
    }
}
