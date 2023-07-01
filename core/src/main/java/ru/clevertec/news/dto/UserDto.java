package ru.clevertec.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO class for <b>User</b> with the following fields:
 * username, authorities
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Schema(description = "Username", requiredMode = Schema.RequiredMode.REQUIRED, example = "dobrowydka")
    private String username;

    @Schema(description = "List of user's authorities", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<AuthorityDto> authorities;
}
