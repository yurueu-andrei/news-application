package ru.clevertec.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.security.controller.open_api.AuthenticationControllerOpenApi;
import ru.clevertec.security.dto.*;
import ru.clevertec.security.service.AuthenticationService;

import java.util.List;

/**
 * Controller for user registration and logging in
 *
 * @author Yuryeu Andrei
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationControllerOpenApi {

    private final AuthenticationService service;

    /**
     * Endpoint, which adds user to DB
     *
     * @param request contains username and password
     * @return returns <b>AuthenticationResponseDto</b> with token
     * @see RegisterRequest
     * @see AuthenticationResponseDto
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    /**
     * Endpoint, used for token update
     *
     * @param request contains username and password
     * @return returns <b>AuthenticationResponseDto</b> with token
     * @see LoginRequest
     * @see AuthenticationResponseDto
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(service.login(request));
    }

    /**
     * Endpoint, used for getting username by provided JWT token
     *
     * @param userDetails current principal
     * @return returns <b>UserDto</b> containing username and authorities
     * @see UserDto
     * @see UserDetails
     */
    @PostMapping("/users")
    @SneakyThrows
    public ResponseEntity<UserDto> findUserByToken(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<AuthorityDto> authorities = userDetails.getAuthorities()
                .stream()
                .map(authority -> new AuthorityDto(authority.getAuthority()))
                .toList();
        UserDto userDto = new UserDto(username, authorities);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}

