package ru.clevertec.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.news.validation.NotBlankIfPresent;

/**
 * DTO class for <b>UPDATING</b> News with the following fields:
 * id, title, text
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsUpdateDto {

    @Schema(description = "Id of News(>=1)", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @Min(1)
    private Long id;

    @Schema(description = "The title of the News(not blank)", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "Cool title")
    @NotBlankIfPresent
    private String title;

    @Schema(description = "The content of the News(not blank)", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "Long and interesting content")
    @NotBlankIfPresent
    private String text;
}
