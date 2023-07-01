package ru.clevertec.news.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.handling.exception.ServiceException;
import ru.clevertec.logging.annotation.Logging;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.CommentListDto;
import ru.clevertec.news.dto.CommentSaveDto;
import ru.clevertec.news.dto.CommentUpdateDto;
import ru.clevertec.news.dto.filter.CommentRequestFilter;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.mapper.CommentMapper;
import ru.clevertec.news.repository.CommentRepository;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

/**
 * Service class for comments with <b>CRUD</b> operations and wrapping into <b>DTO</b>.
 *
 * @author Yuryeu Andrei
 * @see CommentRepository
 * @see CommentMapper
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "Comments")
@Logging
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    /**
     * Method for finding Comment by its ID
     *
     * @param id ID of target entity
     * @return returns a <b>DTO</b> made out of found Comment
     * @throws ServiceException in case of <b>null</b> ID and if Comment not found
     * @see CommentDto
     */
    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public CommentDto findById(Long id) throws ServiceException {
        return commentRepository.findById(id).map(commentMapper::toDto)
                .orElseThrow(() -> new ServiceException("The comment with id = " + id + " was not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Method for finding all Comments with filtering by part of text, username and pagination
     *
     * @param filter POJO with values needed for filtering
     * @return returns a <b>list of DTOs</b> made out of found Comments
     * @see CommentListDto
     */
    @Transactional(readOnly = true)
    public List<CommentListDto> findAll(CommentRequestFilter filter) throws ServiceException {
        try {
            List<Comment> comments = commentRepository.findAll(
                    constructExample(filter),
                    filter.getPageable()
            ).toList();
            return commentMapper.toListDto(comments);
        } catch (Exception ex) {
            throw new ServiceException("The comments were not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Method for finding all Comments of the News with the given id with pagination
     *
     * @param newsId   id of News which Comments we want to find
     * @param pageable used for pagination, defines the number of Comments on the page and the number of page
     * @return returns a <b>List of CommentListDto</b> made out of found Comments
     * @see CommentListDto
     */
    @Transactional(readOnly = true)
    public List<CommentListDto> findAllByNewsId(Long newsId, Pageable pageable) throws ServiceException {
        try {
            List<Comment> comments = commentRepository.findAllByNewsId(newsId, pageable);
            return commentMapper.toListDto(comments);
        } catch (Exception ex) {
            throw new ServiceException("The comments were not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Private method creating Example instance for filtering
     *
     * @param filter filter class, containing username and part of text (both may be null)
     * @return <b>Example</b> - ready to use example for QBE(query by example)
     * @see Example
     */
    private Example<Comment> constructExample(CommentRequestFilter filter) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("username", exact())
                .withMatcher("text", contains().ignoreCase(true));
        Comment comment = new Comment();
        comment.setUsername(filter.getUsername());
        comment.setText(filter.getPartOfText());
        return Example.of(comment, matcher);
    }

    /**
     * Method for adding a new Comment
     *
     * @param commentSaveDto saveDTO with fields needed for add
     * @return returns a DTO of added Comment<b>(contains generated ID)</b>
     * @throws ServiceException in case of <b>null</b> saveDTO
     * @see CommentDto
     * @see CommentSaveDto
     */
    @CachePut(key = "#result.id")
    @Transactional
    public CommentDto add(CommentSaveDto commentSaveDto) throws ServiceException {
        try {
            Comment comment = commentMapper.fromSaveDto(commentSaveDto);
            return commentMapper.toDto(commentRepository.save(comment));
        } catch (Exception ex) {
            throw new ServiceException("The comment was not added", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Method for updating the existing Comment
     *
     * @param commentUpdateDto DTO object with target ID and field(s) <b>to be updated</b>
     * @return <b>CommentDto</b> with updated fields
     * @throws ServiceException in case of update of nonexistent Comment
     * @see CommentUpdateDto
     */
    @CachePut(key = "#commentUpdateDto.id")
    @Transactional
    public CommentDto update(CommentUpdateDto commentUpdateDto) throws ServiceException {
        try {
            Comment comment = commentRepository.findById(commentUpdateDto.getId())
                    .orElseThrow(() ->
                            new ServiceException("The comment to update was not found. Id = " + commentUpdateDto.getId(),
                                    HttpStatus.NOT_FOUND));
            if (commentUpdateDto.getText() != null) {
                comment.setText(commentUpdateDto.getText());
            }

            commentRepository.flush();
            return commentMapper.toDto(comment);
        } catch (Exception ex) {
            throw new ServiceException("The comment with id = " + commentUpdateDto.getId() + " was not updated",
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Method for deleting a Comment
     *
     * @param id ID of target Comment
     * @return <b>true</b> - in case of successful deletion
     * @throws ServiceException in case of <b>null</b> ID
     */
    @CacheEvict(key = "#id")
    @Transactional
    public boolean delete(Long id) {
        try {
            commentRepository.deleteById(id);
            return true;
        } catch (Exception ex) {
            throw new ServiceException("The comment with id = " + id + " was not deleted", HttpStatus.BAD_REQUEST);
        }
    }
}
