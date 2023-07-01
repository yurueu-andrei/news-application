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
import ru.clevertec.news.dto.*;

import java.util.List;

public interface NewsControllerOpenApi {

    @Operation(
            method = "GET",
            tags = "News",
            description = "Find a piece of news by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewsDto.class)
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
                                                "message": "The news with id = 1000 was not found",
                                                "path": "/news/1000"
                                              }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<NewsDto> findById(@PathVariable Long id);

    @Operation(
            method = "GET",
            tags = "News",
            description = "Find news with filtering and pagination",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject("""
                                              [
                                                {
                                                  "id": 1,
                                                  "title": "news crypto title first one",
                                                  "username": "over228",
                                                  "createDate": "2023-04-02T17:26:07.319"
                                                },
                                                {
                                                  "id": 2,
                                                  "title": "news AI title second two",
                                                  "username": "over228",
                                                  "createDate": "2023-04-12T14:43:07.319"
                                                }
                                              ]
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "If no news were found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject("[]")
                            )
                    )
            }
    )
    ResponseEntity<List<NewsListDto>> findAll(
            @RequestParam(required = false) String partOfTitle,
            @RequestParam(required = false) String partOfText,
            @Parameter(example = "{\n\t\"page\": 0,\n\t\"size\": 2\n}") @PageableDefault(size = 5) Pageable pageable
    );

    @Operation(
            method = "POST",
            tags = "News",
            security = @SecurityRequirement(name = "Bearer Authentication", scopes = "WRITE_NEWS"),
            description = "Add news",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewsDto.class)
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
                                                "path": "/news"
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
                                                  "path": "/news"
                                              }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<NewsDto> add(@Valid @RequestBody NewsSaveDto newsSaveDto);

    @Operation(
            method = "PUT",
            security = @SecurityRequirement(name = "Bearer Authentication", scopes = "WRITE_NEWS"),
            tags = "News",
            description = "Update news",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewsDto.class)
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
                                                "path": "/news"
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
                                                "message": "The news to update was not found. Id = 1000",
                                                "path": "/news"
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
                                                  "path": "/news"
                                              }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<NewsDto> update(@Valid @RequestBody NewsUpdateDto newsUpdateDto);

    @Operation(
            method = "DELETE",
            security = @SecurityRequirement(name = "Bearer Authentication", scopes = "DELETE_NEWS"),
            tags = "News",
            description = "Delete news",
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
                                                  "path": "/news/3"
                                              }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Boolean> delete(@PathVariable Long id);

    @Operation(
            method = "GET",
            tags = "Comment",
            description = "Find comments of news by id",
            responses = {
                    @ApiResponse(
                            description = "If news does not have comments",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject("[]")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject("""
                                              [
                                                {
                                                  "id": 35,
                                                  "text": "WOOOW 35",
                                                  "username": "dobrowydka",
                                                  "createDate": "2023-05-21T08:42:33.011519"
                                                },
                                                {
                                                  "id": 36,
                                                  "text": "Cool :) 36",
                                                  "username": "nst.yrk",
                                                  "createDate": "2023-05-25T23:49:45.053382"
                                                }
                                              ]
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<List<CommentListDto>> findCommentsByNewsId(
            @PathVariable Long id,
            @Parameter(example = "{\n\t\"page\": 0,\n\t\"size\": 2\n}") @PageableDefault(size = 5) Pageable pageable
    );

}
