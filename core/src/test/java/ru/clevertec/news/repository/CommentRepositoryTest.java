package ru.clevertec.news.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.entity.News;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

class CommentRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Nested
    class FindById {

        @Test
        void findByIdTest_shouldReturnOptionalOfCommentWithId3AndLoadedNews() {
            //given
            Long id = 3L;
            News news = new News();
            news.setId(3L);
            Optional<Comment> expected = Optional.of(
                    new Comment(3L,
                            "very exiting news", "dobrowydka",
                            LocalDateTime.parse("2023-04-22T19:56:07.319"),
                            news));

            //when
            Optional<Comment> actual = commentRepository.findById(id);

            //then
            Assertions.assertAll(
                    () -> assertEquals(expected, actual),
                    () -> assertEquals(expected.get().getNews().getId(), actual.get().getNews().getId())
            );
        }

        @Test
        void findByIdTest_shouldReturnNullOptional() {
            //given
            Long id = 555L;
            Optional<Comment> expected = Optional.empty();

            //when
            Optional<Comment> actual = commentRepository.findById(id);

            //then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class FindAll {

        @Test
        void findAllTest_shouldReturnCommentsWithPaginationAndWithoutFiltering() {
            //given
            List<Comment> expected = List.of(
                    new Comment(5L,
                            "well done", "dobrowydka",
                            LocalDateTime.parse("2023-04-12T20:20:07.319"), new News()),
                    new Comment(6L,
                            "amazing article", "over1337",
                            LocalDateTime.parse("2023-04-17T11:55:07.319"), new News()));

            Pageable pageable = PageRequest.of(2, 2);
            Example<Comment> example = Example.of(new Comment());

            //when
            List<Comment> actual = commentRepository.findAll(example, pageable).toList();

            //then
            assertEquals(expected, actual);
        }

        @Test
        void findAllTest_shouldReturnCommentsWithPaginationAndWithPartOfTextFiltering() {
            //given
            List<Comment> expected = List.of(
                    new Comment(6L,
                            "amazing article", "over1337",
                            LocalDateTime.parse("2023-04-17T11:55:07.319"), new News()),
                    new Comment(10L,
                            "this article is misinformation", "nst.yrk",
                            LocalDateTime.parse("2023-04-19T02:11:07.319"), new News()));

            Pageable pageable = PageRequest.of(0, 2);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnoreNullValues()
                    .withMatcher("username", exact())
                    .withMatcher("text", contains().ignoreCase(true));
            Comment comment = new Comment();
            comment.setText("article");
            Example<Comment> example = Example.of(comment, matcher);

            //when
            List<Comment> actual = commentRepository.findAll(example, pageable).toList();

            //then
            assertEquals(expected, actual);
        }

        @Test
        void findAllTest_shouldReturnCommentsWithPaginationAndWithUsernameFiltering() {
            //given
            List<Comment> expected = List.of(
                    new Comment(1L,
                            "very cool news", "dobrowydka",
                            LocalDateTime.parse("2023-04-02T22:17:07.319"), new News()),
                    new Comment(3L,
                            "very exiting news", "dobrowydka",
                            LocalDateTime.parse("2023-04-22T19:56:07.319"), new News()));

            Pageable pageable = PageRequest.of(0, 2);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnoreNullValues()
                    .withMatcher("username", exact())
                    .withMatcher("text", contains().ignoreCase(true));
            Comment comment = new Comment();
            comment.setUsername("dobrowydka");
            Example<Comment> example = Example.of(comment, matcher);

            //when
            List<Comment> actual = commentRepository.findAll(example, pageable).toList();

            //then
            assertEquals(expected, actual);
        }

        @Test
        void findAllTest_shouldReturnCommentsWithPaginationAndWithUsernameAndPartOfTextFiltering() {
            //given
            List<Comment> expected = List.of(
                    new Comment(6L,
                            "amazing article", "over1337",
                            LocalDateTime.parse("2023-04-17T11:55:07.319"), new News()));

            Pageable pageable = PageRequest.of(0, 2);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnoreNullValues()
                    .withMatcher("username", exact())
                    .withMatcher("text", contains().ignoreCase(true));
            Comment comment = new Comment();
            comment.setUsername("over1337");
            comment.setText("amazing");
            Example<Comment> example = Example.of(comment, matcher);

            //when
            List<Comment> actual = commentRepository.findAll(example, pageable).toList();

            //then
            assertEquals(expected, actual);
        }

        @Test
        void findAllTest_shouldReturnEmptyListWithPartOfTitleAndPartOfTextFiltering() {
            //given
            List<Comment> expected = new ArrayList<>();
            Pageable pageable = PageRequest.of(0, 3);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnoreNullValues()
                    .withMatcher("username", exact())
                    .withMatcher("text", contains().ignoreCase(true));
            Comment comment = new Comment();
            comment.setUsername("Nonexistent Username");
            comment.setText("Nonexistent Text");
            Example<Comment> example = Example.of(comment, matcher);

            //when
            List<Comment> actual = commentRepository.findAll(example, pageable).toList();

            //then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class findAllByNewsId {

        @Test
        void findAllByNewsIdTest_shouldReturn2CommentsWithNewsId1() {
            //given
            Long newsId = 1L;
            List<Comment> expected = List.of(
                    new Comment(1L,
                            "very cool news", "dobrowydka",
                            LocalDateTime.parse("2023-04-02T22:17:07.319"), new News()),
                    new Comment(4L,
                            "good job!", "nst.yrk",
                            LocalDateTime.parse("2023-04-02T18:40:07.319"), new News()));

            Pageable pageable = PageRequest.of(0, 5);

            //when
            List<Comment> actual = commentRepository.findAllByNewsId(newsId, pageable);
            boolean isAllCommentAreWithGivenNewsId = actual.stream()
                    .allMatch(comment -> comment.getNews().getId().equals(newsId));

            //then
            Assertions.assertAll(
                    () -> assertEquals(expected, actual),
                    () -> assertTrue(isAllCommentAreWithGivenNewsId)
            );
        }

        @Test
        void findAllByNewsIdTest_shouldReturn1CommentWithNewsId5() {
            //given
            Long newsId = 5L;
            List<Comment> expected = List.of(
                    new Comment(5L,
                            "well done", "dobrowydka",
                            LocalDateTime.parse("2023-04-12T20:20:07.319"), new News()));

            Pageable pageable = PageRequest.of(0, 5);

            //when
            List<Comment> actual = commentRepository.findAllByNewsId(newsId, pageable);
            boolean isAllCommentAreWithGivenNewsId = actual.stream()
                    .allMatch(comment -> comment.getNews().getId().equals(newsId));

            //then
            Assertions.assertAll(
                    () -> assertEquals(expected, actual),
                    () -> assertTrue(isAllCommentAreWithGivenNewsId)
            );
        }
    }
}