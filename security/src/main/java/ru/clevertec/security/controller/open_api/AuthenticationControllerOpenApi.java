package ru.clevertec.security.controller.open_api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.handling.handler.message.ExceptionMessage;
import ru.clevertec.security.dto.AuthenticationResponseDto;
import ru.clevertec.security.dto.LoginRequest;
import ru.clevertec.security.dto.RegisterRequest;
import ru.clevertec.security.dto.UserDto;

public interface AuthenticationControllerOpenApi {

    @Operation(
            method = "POST",
            tags = "Register",
            description = "Register a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                               "token": "45yv356vy536.4rtds34rshgt5b.76iytjghghjyutir7tuwefhus8w",
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionMessage.class),
                                    examples = @ExampleObject("""
                                            {
                                                "timestamp": "2023-06-28T16:56:10.3894402"
                                                "status": 403
                                                "error": "Forbidden"
                                                "message": "Error while registration"
                                                "path": "/auth/register"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionMessage.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "timestamp": "2023-06-28T16:58:36.807188",
                                                 "status": 400,
                                                 "error": "Bad Request",
                                                 "message": "Username is already exists",
                                                 "path": "/auth/register"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<AuthenticationResponseDto> register(@RequestBody RegisterRequest request);

    @Operation(
            method = "POST",
            tags = "Login",
            description = "Authorize an existing user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                               "token": "45yv356vy536.4rtds34rshgt5b.76iytjghghjyutir7tuwefhus8w",
                                             }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionMessage.class),
                                    examples = @ExampleObject("""
                                            {
                                                "timestamp": "2023-06-28T16:56:10.3894402"
                                                "status": 403
                                                "error": "Forbidden"
                                                "message": "Username not found"
                                                "path": "/auth/login"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionMessage.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "timestamp": "2023-06-28T16:58:36.807188",
                                                 "status": 400,
                                                 "error": "Bad Request",
                                                 "message": "Username does not exist",
                                                 "path": "/auth/register"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody LoginRequest request);

    @Operation(
            method = "POST",
            tags = "Find user by token(used only for feign client)",
            description = "Get username and authorities of the JWT token subject",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                "username": "dobrowydka",
                                                "authorities": [
                                                    {
                                                    "name": "WRITE_COMMENTS"
                                                    }
                                                ]
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<UserDto> findUserByToken(@AuthenticationPrincipal UserDetails userDetails);
}
