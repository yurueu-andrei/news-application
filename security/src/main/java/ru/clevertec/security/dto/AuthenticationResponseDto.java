package ru.clevertec.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO class for <b>authentication response</b> with the token
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
public class AuthenticationResponseDto {

    @Schema(description = "JWT token", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;
}
