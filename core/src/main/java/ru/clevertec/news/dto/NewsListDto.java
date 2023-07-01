package ru.clevertec.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO class for <b>LIST OF</b> News with the following fields:
 * id, title, text, username, createDate
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsListDto implements Serializable {

    @Schema(description = "Id of News(>=1)", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @Min(1)
    private Long id;

    @Schema(description = "The title of the News", requiredMode = Schema.RequiredMode.REQUIRED, example = "Cool title")
    private String title;

    @Schema(description = "Username of the user, who wrote the News", requiredMode = Schema.RequiredMode.REQUIRED, example = "dobrowydka")
    private String username;

    @Schema(description = "Date of News creation", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-04-26 11:30:07.319")
    private LocalDateTime createDate;
}
