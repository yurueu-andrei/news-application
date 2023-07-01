package ru.clevertec.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO class for <b>SINGLE</b> Comment with the following fields:
 * id, text, username, createDate and News associated with the Comment
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Serializable {

    @Schema(description = "Id of Comment(>=1)", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @Min(1)
    private Long id;

    @Schema(description = "The content of the Comment", requiredMode = Schema.RequiredMode.REQUIRED, example = "Amazing!")
    private String text;

    @Schema(description = "Username of the user, who wrote the Comment", requiredMode = Schema.RequiredMode.REQUIRED, example = "dobrowydka")
    private String username;

    @Schema(description = "Date of Comment creation", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-04-26 11:30:07.319")
    private LocalDateTime createDate;

    @Schema(description = "News to which Comment was written", requiredMode = Schema.RequiredMode.REQUIRED)
    private NewsListDto news;
}
