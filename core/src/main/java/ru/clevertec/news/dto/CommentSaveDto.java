package ru.clevertec.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for <b>SAVING</b> Comment with the following fields:
 * text, username <b>(both must contain at least one non-whitespace character)</b>
 * and id of the News associated with the Comment
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentSaveDto {

    @Schema(description = "The content of the Comment(not blank)", requiredMode = Schema.RequiredMode.REQUIRED, example = "Amazing!")
    @NotBlank
    private String text;

    @Schema(description = "Username of the user, who wrote the Comment(not blank)", requiredMode = Schema.RequiredMode.REQUIRED, example = "dobrowydka")
    @NotBlank
    private String username;

    @Schema(description = "Id of News to which Comment was written", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    private Long newsId;
}
