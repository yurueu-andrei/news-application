package ru.clevertec.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for <b>Authority</b> with the name field
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDto {

    @Schema(description = "Name of the authority", requiredMode = Schema.RequiredMode.REQUIRED, example = "WRITE_COMMENTS")
    private String name;
}
