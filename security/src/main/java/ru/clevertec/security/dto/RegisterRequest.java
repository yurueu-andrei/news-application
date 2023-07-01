package ru.clevertec.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.clevertec.security.entity.Role;

/**
 * DTO class for <b>Registration</b> with the following fields:
 * name, surname, username, password and role
 *
 * @author Yuryeu Andrei
 * @see Role
 */
@Data
@AllArgsConstructor
public class RegisterRequest {

    @Schema(description = "Name of the user(not blank)", requiredMode = Schema.RequiredMode.REQUIRED, example = "Andrei")
    @NotBlank
    private String name;

    @Schema(description = "Surname of the user(not blank)", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yurueu")
    @NotBlank
    private String surname;

    @Schema(description = "Username(not blank)", requiredMode = Schema.RequiredMode.REQUIRED, example = "dobrowydka")
    @NotBlank
    private String username;

    @Schema(description = "Password(not blank, minimum 6 symbols)", requiredMode = Schema.RequiredMode.REQUIRED, example = "qwerty123")
    @NotBlank
    @Length(min = 6)
    private String password;

    @Schema(description = "Role", requiredMode = Schema.RequiredMode.REQUIRED, example = "ADMIN")
    private Role role;
}
