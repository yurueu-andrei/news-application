package ru.clevertec.news.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.handling.exception.ServiceException;
import ru.clevertec.news.controller.open_api.CommentControllerOpenApi;
import ru.clevertec.news.dto.*;
import ru.clevertec.news.dto.filter.CommentRequestFilter;
import ru.clevertec.news.service.CommentService;

import java.util.List;

/**
 * Controller for Comments with <b>CRUD</b> operations
 *
 * @author Yuryeu Andrei
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/comments")
public class CommentController implements CommentControllerOpenApi {

    private final CommentService commentService;

    /**
     * Endpoint, which finds the Comment entity and return it as CommentDto
     *
     * @param id ID of target entity
     * @return returns <b>CommentDto</b> made out of found Comment entity
     * @see CommentDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(commentService.findById(id), HttpStatus.OK);
    }

    /**
     * Endpoint, which finds all News and return it as CommentListDtos
     *
     * @param username   used for filtering(optional), defines username of a comment creator,
     *                   which must be present in resulting CommentListDtos
     * @param partOfText used for filtering(optional), defines part of a comment text,
     *                   which must be present in resulting CommentListDtos
     * @param pageable   used for pagination, defines the number of Comments on the page and the number of page
     * @return returns <b>CommentListDtos</b> made out of found Comments
     * @see NewsListDto
     */
    @GetMapping
    public ResponseEntity<List<CommentListDto>> findAll(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String partOfText,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        return new ResponseEntity<>(commentService.findAll(
                new CommentRequestFilter(username, partOfText, pageable)), HttpStatus.OK);
    }

    /**
     * Endpoint, which adds Comment entity
     *
     * @param commentSaveDto contains all the fields needed to create Comment entity
     *                       (text and username mustn't be null or blank)
     * @return returns created entity as <b>CommentDto</b>
     * @see NewsSaveDto
     */
    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_COMMENTS')")
    public ResponseEntity<CommentDto> add(@Valid @RequestBody CommentSaveDto commentSaveDto) {
        return new ResponseEntity<>(commentService.add(commentSaveDto), HttpStatus.CREATED);
    }

    /**
     * Endpoint, which updates existing Comment entity
     *
     * @param commentUpdateDto contains id and the fields to be updated
     * @return returns <b>CommentDto</b> (only true in case of successful operation)
     * @throws ServiceException in case of failure
     * @see NewsUpdateDto
     */
    @PutMapping
    @PreAuthorize("hasAuthority('WRITE_COMMENTS')")
    public ResponseEntity<CommentDto> update(@Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        return new ResponseEntity<>(commentService.update(commentUpdateDto), HttpStatus.OK);
    }

    /**
     * Endpoint, which deletes existing Comment entity
     *
     * @param id id of the entity to be deleted
     * @return returns <b>boolean</b> value (only true in case of successful operation)
     * @throws ServiceException in case of failure
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_COMMENTS')")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return new ResponseEntity<>(commentService.delete(id), HttpStatus.OK);
    }
}
