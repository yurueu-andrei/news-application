package ru.clevertec.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for <b>SAVING</b> News with the following fields:
 * title, text, username <b>(must contain at least one non-whitespace character)</b>
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsSaveDto {

    @Schema(description = "The title of the News(not blank)", requiredMode = Schema.RequiredMode.REQUIRED, example = "Cool title")
    @NotBlank
    private String title;

    @Schema(description = "The content of the News(not blank)", requiredMode = Schema.RequiredMode.REQUIRED, example = "Long and interesting content")
    @NotBlank
    private String text;

    @Schema(description = "Username of the user, who wrote the News(not blank)", requiredMode = Schema.RequiredMode.REQUIRED, example = "dobrowydka")
    @NotBlank
    private String username;
}
