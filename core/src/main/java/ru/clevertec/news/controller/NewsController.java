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
import ru.clevertec.news.controller.open_api.NewsControllerOpenApi;
import ru.clevertec.news.dto.*;
import ru.clevertec.news.dto.filter.NewsRequestFilter;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.service.NewsService;

import java.util.List;

/**
 * Controller for News with <b>CRUD</b> operations
 *
 * @author Yuryeu Andrei
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/news")
public class NewsController implements NewsControllerOpenApi {

    private final NewsService newsService;
    private final CommentService commentService;

    /**
     * Endpoint, which finds the News entity and return it as NewsDto
     *
     * @param id ID of target entity
     * @return returns <b>NewsDto</b> made out of found News entity
     * @see NewsDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(newsService.findById(id), HttpStatus.OK);
    }

    /**
     * Endpoint, which finds all News and return it as NewsListDtos
     *
     * @param partOfTitle used for filtering(optional), defines part of a news title,
     *                    which must be present in resulting NewsListDtos
     * @param partOfText  used for filtering(optional), defines part of a news text,
     *                    which must be present in resulting NewsListDtos
     * @param pageable    used for pagination, defines the number of News on the page and the number of page
     * @return returns <b>NewsListDtos</b> made out of found News
     * @see NewsListDto
     */
    @GetMapping
    public ResponseEntity<List<NewsListDto>> findAll(
            @RequestParam(required = false) String partOfTitle,
            @RequestParam(required = false) String partOfText,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        return new ResponseEntity<>(newsService.findAll(
                new NewsRequestFilter(
                        partOfTitle,
                        partOfText,
                        pageable
                )), HttpStatus.OK);
    }

    /**
     * Endpoint, which adds News entity
     *
     * @param newsSaveDto contains all the fields needed to create News entity
     *                    (title and text mustn't be null or blank)
     * @return returns created entity as <b>NewsDto</b>
     * @see NewsSaveDto
     */
    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_NEWS')")
    public ResponseEntity<NewsDto> add(@Valid @RequestBody NewsSaveDto newsSaveDto) {
        return new ResponseEntity<>(newsService.add(newsSaveDto), HttpStatus.CREATED);
    }

    /**
     * Endpoint, which updates existing News entity
     *
     * @param newsUpdateDto contains id and the fields to be updated
     * @return returns <b>NewsDto</b> (only true in case of successful operation)
     * @throws ServiceException in case of failure
     * @see NewsUpdateDto
     */
    @PutMapping
    @PreAuthorize("hasAuthority('WRITE_NEWS')")
    public ResponseEntity<NewsDto> update(@Valid @RequestBody NewsUpdateDto newsUpdateDto) {
        return new ResponseEntity<>(newsService.update(newsUpdateDto), HttpStatus.OK);
    }

    /**
     * Endpoint, which deletes existing News entity
     *
     * @param id id of the entity to be deleted
     * @return returns <b>boolean</b> value (only true in case of successful operation)
     * @throws ServiceException in case of failure
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_NEWS')")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return new ResponseEntity<>(newsService.delete(id), HttpStatus.OK);
    }

    /**
     * Endpoint, which finds the Comment of particular News with pagination
     *
     * @param id ID of News, which Comment we want to get
     * @return returns <b>List of CommentListDto</b> made out of found Comments
     * @see CommentListDto
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentListDto>> findCommentsByNewsId(
            @PathVariable Long id,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        return new ResponseEntity<>(commentService.findAllByNewsId(id, pageable), HttpStatus.OK);
    }
}
