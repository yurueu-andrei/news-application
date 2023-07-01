package ru.clevertec.security.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.handling.exception.LoginException;
import ru.clevertec.handling.exception.RegisterException;
import ru.clevertec.security.dto.AuthenticationResponseDto;
import ru.clevertec.security.dto.LoginRequest;
import ru.clevertec.security.dto.RegisterRequest;
import ru.clevertec.security.entity.Role;
import ru.clevertec.security.entity.User;
import ru.clevertec.security.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Nested
    class RegisterTests {

        @Test
        void registerTest_shouldReturnJWTTokenGeneratedByJwtService() {
            //given
            RegisterRequest request = new RegisterRequest(
                    "name",
                    "surname",
                    "username",
                    "password",
                    Role.ADMIN
            );
            AuthenticationResponseDto expected = new AuthenticationResponseDto("Bearer 12345");
            User userWithoutId = User.builder()
                    .name(request.getName())
                    .surname(request.getSurname())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .build();

            User userWithId = User.builder()
                    .id(10L)
                    .name(userWithoutId.getName())
                    .surname(userWithoutId.getSurname())
                    .username(userWithoutId.getUsername())
                    .password(userWithoutId.getPassword())
                    .role(userWithoutId.getRole())
                    .build();

            //when
            when(userRepository.save(userWithoutId)).thenReturn(userWithId);
            when(jwtService.generateToken(userWithoutId)).thenReturn("Bearer 12345");
            AuthenticationResponseDto actual = authenticationService.register(request);

            //then
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void registerTest_shouldThrowRegisterException() {
            //given
            RegisterRequest request = new RegisterRequest(
                    "name",
                    "surname",
                    "username",
                    "password",
                    Role.ADMIN
            );
            User userWithoutId = User.builder()
                    .name(request.getName())
                    .surname(request.getSurname())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .build();

            //when
            when(userRepository.save(userWithoutId))
                    .thenThrow(new RegisterException("Username is already exists", HttpStatus.BAD_REQUEST));

            //then
            Assertions.assertThrows(RegisterException.class, () -> authenticationService.register(request));
        }
    }

    @Nested
    class LoginTests {

        @Test
        void loginTest_shouldAuthorizeUserAndReturnJWTTokenGeneratedByJwtService() {
            //given
            LoginRequest request = new LoginRequest(
                    "username",
                    "password"
            );
            AuthenticationResponseDto expected = new AuthenticationResponseDto("Bearer 12345");
            var token = new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            );

            Optional<User> user = Optional.of(new User(
                    1L,
                    "andrei",
                    "yurueu",
                    "dobrowydka",
                    "12345",
                    Role.ADMIN));

            //when
            when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
            when(authenticationManager.authenticate(token)).thenReturn(null);
            when(jwtService.generateToken(user.get())).thenReturn("Bearer 12345");
            AuthenticationResponseDto actual = authenticationService.login(request);

            //then
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void loginTest_shouldThrowLoginException() {
            //given
            LoginRequest request = new LoginRequest(
                    "username",
                    "password"
            );

            //when
            when(userRepository.findByUsername(request.getUsername()))
                    .thenThrow(new LoginException("Username does not exist", HttpStatus.BAD_REQUEST));

            //then
            Assertions.assertThrows(LoginException.class, () -> authenticationService.login(request));
        }
    }
}