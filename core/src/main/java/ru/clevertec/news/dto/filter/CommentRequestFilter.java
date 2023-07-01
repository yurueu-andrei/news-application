package ru.clevertec.news.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

/**
 * DTO class for transferring part of text, username and Pageable for <b>findAll method for Comments</b>
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestFilter {

    @Schema(description = "Filtering by part of the text parameter(not blank)", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "hello world")
    @NotBlank
    private String partOfText;

    @Schema(description = "Filtering by username parameter(not blank)", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "dobrowydka")
    @NotBlank
    private String username;

    @Schema(description = "Pagination parameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "page=0&size=5", example = "page=0&size=5")
    private Pageable pageable;
}
