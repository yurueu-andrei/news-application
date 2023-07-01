package ru.clevertec.news.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.handling.exception.ModificationException;
import ru.clevertec.news.client.UserClient;
import ru.clevertec.news.dto.*;
import ru.clevertec.news.security.request.RequestWrapper;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.service.NewsService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Username validation filter class, used to have possibility of removing only those News and Comments,
 * that belong to the current user
 *
 * @author Yuryeu Andrei
 */
@Component
@RequiredArgsConstructor
public class UsernameValidationFilter extends OncePerRequestFilter {

    private final UserClient userClient;
    private final NewsService newsService;
    private final CommentService commentService;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        List<AuthorityDto> authorities = userClient.getUserDetails(header).getAuthorities();
        //ADMIN HAS >3 AUTHORITIES, HE CAN MODIFY EVERYTHING
        if (authorities.size() > 3) {
            filterChain.doFilter(request, response);
            return;
        }

        // POST validation
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            RequestWrapper servletRequest = new RequestWrapper(request);
            String requestBody = extractRequestBody(servletRequest);
            String username = userClient.getUserDetails(header).getUsername();
            if ("/news".equalsIgnoreCase(request.getRequestURI()) &&
                    authorities.stream().anyMatch(e -> "WRITE_NEWS".equals(e.getName()))) {
                NewsSaveDto news = mapper.readValue(requestBody, NewsSaveDto.class);
                validateUsername(news.getUsername(), username);
            }

            if ("/comments".equalsIgnoreCase(request.getRequestURI()) &&
                    authorities.stream().anyMatch(e -> "WRITE_COMMENTS".equals(e.getName()))) {
                CommentSaveDto comment = mapper.readValue(requestBody, CommentSaveDto.class);
                validateUsername(comment.getUsername(), username);
            }
            filterChain.doFilter(servletRequest, response);
            return;
        }

        // PUT validation
        if ("PUT".equalsIgnoreCase(request.getMethod())) {
            RequestWrapper servletRequest = new RequestWrapper(request);
            String requestBody = extractRequestBody(servletRequest);

            String username = userClient.getUserDetails(header).getUsername();
            if ("/news".equalsIgnoreCase(request.getRequestURI()) &&
                    authorities.stream().anyMatch(e -> "WRITE_NEWS".equals(e.getName()))) {
                NewsUpdateDto news = mapper.readValue(requestBody, NewsUpdateDto.class);
                NewsDto newsInDB = newsService.findById(news.getId());
                validateUsername(newsInDB.getUsername(), username);
            }

            if ("/comments".equalsIgnoreCase(request.getRequestURI()) &&
                    authorities.stream().anyMatch(e -> "WRITE_COMMENTS".equals(e.getName()))) {
                CommentUpdateDto comment = mapper.readValue(requestBody, CommentUpdateDto.class);
                CommentDto commentInDB = commentService.findById(comment.getId());
                validateUsername(commentInDB.getUsername(), username);
            }
            filterChain.doFilter(servletRequest, response);
            return;
        }

        // DELETE validation
        if ("DELETE".equalsIgnoreCase(request.getMethod())) {
            String[] URIElements = request.getRequestURI().split("/");
            if (URIElements.length != 3) {
                filterChain.doFilter(request, response);
                return;
            }

            String username = userClient.getUserDetails(header).getUsername();
            Long id = Long.valueOf(URIElements[2]);
            if ("news".equalsIgnoreCase(URIElements[1]) &&
                    authorities.stream().anyMatch(e -> "DELETE_NEWS".equals(e.getName()))) {
                NewsDto news = newsService.findById(id);
                validateUsername(news.getUsername(), username);
            }

            if ("comments".equalsIgnoreCase(URIElements[1]) &&
                    authorities.stream().anyMatch(e -> "DELETE_COMMENTS".equals(e.getName()))) {
                CommentDto comment = commentService.findById(id);
                validateUsername(comment.getUsername(), username);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void validateUsername(String entityUsername, String username) {
        if (!entityUsername.equalsIgnoreCase(username)) {
            throw new ModificationException("Unable to modify someone's else item", HttpStatus.FORBIDDEN);
        }
    }

    private String extractRequestBody(ServletRequest request) throws IOException {
        try (Scanner s = new Scanner(request.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }
}
