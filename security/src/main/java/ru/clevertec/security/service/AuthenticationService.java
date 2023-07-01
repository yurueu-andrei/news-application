package ru.clevertec.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.handling.exception.LoginException;
import ru.clevertec.handling.exception.RegisterException;
import ru.clevertec.logging.annotation.Logging;
import ru.clevertec.security.dto.AuthenticationResponseDto;
import ru.clevertec.security.dto.LoginRequest;
import ru.clevertec.security.dto.RegisterRequest;
import ru.clevertec.security.entity.User;
import ru.clevertec.security.repository.UserRepository;

/**
 * Service class for authentication with register and login methods
 *
 * @author Yuryeu Andrei
 * @see PasswordEncoder
 * @see JwtService
 */
@Service
@RequiredArgsConstructor
@Logging
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Method for registration(saves user to db with encrypted password)
     *
     * @param request RegisterRequest DTO
     * @return returns a <b>AuthenticationResponseDto</b> with generated JWT token
     * @see RegisterRequest
     * @see AuthenticationResponseDto
     * @see JwtService
     */
    public AuthenticationResponseDto register(RegisterRequest request) {
        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new RegisterException("Username is already exists", HttpStatus.BAD_REQUEST);
        }
        String token = jwtService.generateToken(user);
        return new AuthenticationResponseDto(token);
    }

    /**
     * Method for logging in(checks the presence of given user in DB) and generates token for it
     *
     * @param request LoginRequest DTO
     * @return returns a <b>AuthenticationResponseDto</b> with generated JWT token
     * @see LoginRequest
     * @see AuthenticationResponseDto
     * @see JwtService
     */
    public AuthenticationResponseDto login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new LoginException("Username does not exist", HttpStatus.BAD_REQUEST));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        String token = jwtService.generateToken(user);
        return new AuthenticationResponseDto(token);
    }
}
