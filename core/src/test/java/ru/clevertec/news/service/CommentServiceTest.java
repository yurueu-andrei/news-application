package ru.clevertec.news.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.clevertec.handling.exception.ServiceException;
import ru.clevertec.news.dto.*;
import ru.clevertec.news.dto.filter.CommentRequestFilter;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.mapper.CommentMapper;
import ru.clevertec.news.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Spy
    private CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @InjectMocks
    private CommentService commentService;

    @Nested
    class FindById {

        @Test
        void findByIdTest_shouldReturnCommentDtoWithId1() {
            //given
            Long id = 1L;
            Comment comment = new Comment(1L,
                    "very cool news", "dobrowydka",
                    LocalDateTime.parse("2023-04-02T22:17:07.319"), new News());
            CommentDto expected = new CommentDto(comment.getId(),
                    comment.getText(),
                    comment.getUsername(),
                    comment.getCreateDate(),
                    new NewsListDto());

            //when
            when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
            CommentDto actual = commentService.findById(id);

            //then
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void findByIdTest_shouldThrowServiceExceptionForNonExistentComment() {
            //given
            Long id = 55L;

            //when
            when(commentRepository.findById(id)).thenReturn(Optional.empty());

            //then
            Assertions.assertThrows(ServiceException.class, () -> commentService.findById(id));
        }
    }

    @Test
    void findAllByNewsIdTest_shouldReturnCommentDtoWithId1() {
        //given
        Long newsId = 1L;
        News news = new News();
        news.setId(1L);
        List<Comment> comments = List.of(
                new Comment(1L,
                        "very cool news", "dobrowydka",
                        LocalDateTime.parse("2023-04-02T22:17:07.319"), news),
                new Comment(3L,
                        "very exiting news", "dobrowydka",
                        LocalDateTime.parse("2023-04-22T19:56:07.319"), news));

        List<CommentListDto> expected = comments
                .stream()
                .map(comment -> new CommentListDto(comment.getId(), comment.getText(), comment.getUsername(), comment.getCreateDate()))
                .toList();

        PageRequest pageable = PageRequest.of(0, 2);

        //when
        when(commentRepository.findAllByNewsId(newsId, pageable)).thenReturn(comments);
        List<CommentListDto> actual = commentService.findAllByNewsId(newsId, pageable);

        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findAllTest_shouldReturnAllCommentsWithIdFrom4To6() {
        //given
        Pageable pageable = PageRequest.of(1, 3);
        List<Comment> news = List.of(new Comment(4L,
                        "good job!", "nst.yrk",
                        LocalDateTime.parse("2023-04-02T18:40:07.319"), new News()),
                new Comment(5L,
                        "well done", "dobrowydka",
                        LocalDateTime.parse("2023-04-12T20:20:07.319"), new News()),
                new Comment(6L,
                        "amazing article", "over1337",
                        LocalDateTime.parse("2023-04-17T11:55:07.319"), new News()));
        List<CommentListDto> expected = news
                .stream()
                .map(e -> new CommentListDto(e.getId(), e.getText(), e.getUsername(), e.getCreateDate()))
                .toList();

        CommentRequestFilter filter = new CommentRequestFilter();
        filter.setPageable(pageable);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("username", exact())
                .withMatcher("text", contains().ignoreCase(true));
        Comment forExample = new Comment();
        forExample.setUsername(filter.getUsername());
        forExample.setText(filter.getPartOfText());
        Example<Comment> example = Example.of(forExample, matcher);

        //when
        when(commentRepository.findAll(example, pageable)).thenReturn(new PageImpl<>(news));
        List<CommentListDto> actual = commentService.findAll(filter);

        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void addTest_shouldAddCommentAndReturnCommentDto() {
        //given
        LocalDateTime date = LocalDateTime.now();
        Comment commentWithoutId = new Comment(null, "hello", "hi", null, new News());
        Comment commentWithId = new Comment(11L, "hello", "hi", date, new News());
        CommentDto expected = new CommentDto(11L, "hello", "hi", date, new NewsListDto());
        CommentSaveDto commentSaveDto = new CommentSaveDto("hello", "hi", null);

        //when
        when(commentRepository.save(commentWithoutId)).thenReturn(commentWithId);
        CommentDto actual = commentService.add(commentSaveDto);

        //then
        Assertions.assertEquals(expected, actual);
    }

    @Nested
    class Update {

        @Test
        void updateTest_shouldUpdateCommentAndReturnCommentDtoInCaseOfSuccessfulUpdate() {
            //given
            Long id = 3L;
            Comment comment = new Comment(3L,
                    "very exiting news", "dobrowydka",
                    LocalDateTime.parse("2023-04-22T19:56:07.319"), new News());
            CommentUpdateDto commentUpdateDto = new CommentUpdateDto(3L, "hello");
            CommentDto expected = new CommentDto(3L, "hello", "dobrowydka",
                    LocalDateTime.parse("2023-04-22T19:56:07.319"),
                    new NewsListDto());

            //when
            when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
            CommentDto actual = commentService.update(commentUpdateDto);

            //then
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void updateTest_shouldThrowServiceExceptionInCaseOfUnsuccessfulUpdate() {
            //given
            Long id = 3L;
            CommentUpdateDto commentUpdateDto = new CommentUpdateDto(3L, "hello");

            //when
            when(commentRepository.findById(id)).thenReturn(Optional.empty());

            //then
            Assertions.assertThrows(ServiceException.class, () -> commentService.update(commentUpdateDto));
        }
    }

    @Test
    void deleteTest_shouldReturnTrueInCaseOfSuccessfulDelete() {
        //given
        Long id = 3L;

        //when
        boolean actual = commentService.delete(id);

        //then
        Mockito.verify(commentRepository, Mockito.times(1))
                .deleteById(Mockito.any());
        Assertions.assertTrue(actual);
    }
}