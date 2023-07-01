package ru.clevertec.news.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

/**
 * DTO class for transferring part of title, part of text and Pageable for <b>findAll method for News</b>
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsRequestFilter {

    @Schema(description = "Filtering by part of the title parameter(not blank)", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "NBA")
    @NotBlank
    private String partOfTitle;

    @Schema(description = "Filtering by part of the text parameter(not blank)", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "basketball")
    @NotBlank
    private String partOfText;

    @Schema(description = "Pagination parameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "page=0&size=5", example = "page=0&size=5")
    private Pageable pageable;
}
