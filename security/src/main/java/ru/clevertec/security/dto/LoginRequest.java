package ru.clevertec.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * DTO class for <b>Logging in</b> with the following fields:
 * username, password
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
public class LoginRequest {

    @Schema(description = "Username(not blank)", requiredMode = Schema.RequiredMode.REQUIRED, example = "dobrowydka")
    @NotBlank
    private String username;

    @Schema(description = "Password(not blank, minimum 6 symbols)", requiredMode = Schema.RequiredMode.REQUIRED, example = "qwerty123")
    @NotBlank
    @Length(min = 6)
    private String password;
}
