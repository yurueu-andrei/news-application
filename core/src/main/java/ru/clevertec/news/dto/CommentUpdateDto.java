package ru.clevertec.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for <b>UPDATING</b> Comment with the following fields:
 * id, text
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateDto {

    @Schema(description = "Id of Comment(>=1)", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @Min(1)
    private Long id;

    @Schema(description = "The content of the Comment(not blank)", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "Amazing!")
    @NotBlank
    private String text;
}
