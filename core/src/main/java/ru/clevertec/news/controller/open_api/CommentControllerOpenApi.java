package ru.clevertec.news.controller.open_api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.handling.handler.message.ExceptionMessage;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.CommentListDto;
import ru.clevertec.news.dto.CommentSaveDto;
import ru.clevertec.news.dto.CommentUpdateDto;

import java.util.List;

public interface CommentControllerOpenApi {

    @Operation(
            method = "GET",
            tags = "Comment",
            description = "Find a comment by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionMessage.class),
                                    examples = @ExampleObject("""
                                              {
                                                "timestamp": "2023-06-28T21:18:45.284886",
                                                "status": 404,
                                                "error": "Not Found",
                                                "message": "The comment with id = 1000 was not found",
                                                "path": "/comments/1000"
                                              }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<CommentDto> findById(@PathVariable Long id);

    @Operation(
            method = "GET",
            tags = "Comment",
            description = "Find comments with filtering and pagination",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject("""
                                              [
                                                {
                                                  "id": 1,
                                                  "text": "I dont like it(( 1",
                                                  "username": "idprodark",
                                                  "createDate": "2023-05-12T01:05:04.016274"
                                                },
                                                {
                                                  "id": 2,
                                                  "text": "Well done! 2",
                                                  "username": "yaros1337",
                                                  "createDate": "2023-05-16T08:30:53.039881"
                                                }
                                              ]
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "If no comments were found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject("[]")
                            )
                    )
            }
    )
    ResponseEntity<List<CommentListDto>> findAll(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String partOfText,
            @Parameter(example = "{\n\t\"page\": 0,\n\t\"size\": 2\n}") @PageableDefault(size = 5) Pageable pageable
    );

    @Operation(
            method = "POST",
            tags = "Comment",
            security = @SecurityRequirement(name = "Bearer Authentication", scopes = "WRITE_COMMENTS"),
            description = "Add comments",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionMessage.class),
                                    examples = @ExampleObject("""
                                              {
                                                "timestamp": "2023-06-28T21:35:24.6449012",
                                                "status": 400,
                                                "error": "Bad Request",
                                                "message": "Arguments are not valid",
                                                "path": "/comments"
                                              }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionMessage.class),
                                    examples = @ExampleObject("""
                                              {
                                                  "timestamp": "2023-06-28T21:47:21.47112",
                                                  "status": 400,
                                                  "error": "Bad Request",
                                                  "message": "Access Denied",
                                                  "path": "/comments"
                                              }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<CommentDto> add(@Valid @RequestBody CommentSaveDto commentSaveDto);

    @Operation(
            method = "PUT",
            security = @SecurityRequirement(name = "Bearer Authentication", scopes = "WRITE_COMMENTS"),
            tags = "Comment",
            description = "Update comment",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionMessage.class),
                                    examples = @ExampleObject("""
                                              {
                                                "timestamp": "2023-06-28T21:35:24.6449012",
                                                "status": 400,
                                                "error": "Bad Request",
                                                "message": "Arguments are not valid",
                                                "path": "/comments"
                                              }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionMessage.class),
                                    examples = @ExampleObject("""
                                              {
                                                "timestamp": "2023-06-28T21:35:24.6449012",
                                                "status": 404,
                                                "error": "Not found",
                                                "message": "The comment to update was not found. Id = 1000",
                                                "path": "/comments"
                                              }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionMessage.class),
                                    examples = @ExampleObject("""
                                              {
                                                  "timestamp": "2023-06-28T21:47:21.47112",
                                                  "status": 400,
                                                  "error": "Bad Request",
                                                  "message": "Access Denied",
                                                  "path": "/comments"
                                              }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<CommentDto> update(@Valid @RequestBody CommentUpdateDto commentUpdateDto);

    @Operation(
            method = "DELETE",
            security = @SecurityRequirement(name = "Bearer Authentication", scopes = "DELETE_COMMENTS"),
            tags = "Comment",
            description = "Delete comment",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject("true")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionMessage.class),
                                    examples = @ExampleObject("""
                                              {
                                                  "timestamp": "2023-06-28T21:47:21.47112",
                                                  "status": 400,
                                                  "error": "Bad Request",
                                                  "message": "Access Denied",
                                                  "path": "/comments/3"
                                              }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Boolean> delete(@PathVariable Long id);

}
