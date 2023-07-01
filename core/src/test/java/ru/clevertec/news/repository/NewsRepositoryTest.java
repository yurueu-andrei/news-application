package ru.clevertec.news.repository;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.entity.News;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

class NewsRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private NewsRepository newsRepository;

    @Nested
    class FindAll {

        @Test
        void findAllTest_shouldReturnNewsWithPaginationAndWithoutFiltering() {
            //given
            List<News> expected = List.of(
                    new News(3L, "news Ukraine title third three",
                            "text of the third news about Ukraine", "username3",
                            LocalDateTime.parse("2023-04-22T09:23:07.319"),
                            new ArrayList<>()),
                    new News(4L, "news AI title fourth four",
                            "text of the fourth news about AI", "username4",
                            LocalDateTime.parse("2023-04-17T11:34:07.319"),
                            new ArrayList<>()));
            Pageable pageable = PageRequest.of(1, 2);
            Example<News> example = Example.of(new News());

            //when
            List<News> actual = newsRepository.findAll(example, pageable).toList();

            //then
            assertEquals(expected, actual);
        }

        @Test
        void findAllTest_shouldReturnNewsWithPaginationAndWithPartOfTitleFiltering() {
            //given
            List<News> expected = List.of(
                    new News(1L, "news crypto title first one",
                            "text of the first news about crypto", "username1",
                            LocalDateTime.parse("2023-04-02T17:26:07.319"),
                            new ArrayList<>()),
                    new News(6L, "news crypto title sixth six",
                            "text of the sixth news about crypto", "username6",
                            LocalDateTime.parse("2023-04-18T11:23:07.319"),
                            new ArrayList<>()));
            Pageable pageable = PageRequest.of(0, 2);
            News news = new News();
            news.setTitle("news crypto title");
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnoreNullValues()
                    .withMatcher("title", contains().ignoreCase(true))
                    .withMatcher("text", contains().ignoreCase(true));
            Example<News> example = Example.of(news, matcher);

            //when
            List<News> actual = newsRepository.findAll(example, pageable).toList();

            //then
            assertEquals(expected, actual);
        }

        @Test
        void findAllTest_shouldReturnNewsWithPaginationAndWithPartOfTextFiltering() {
            //given
            List<News> expected = List.of(
                    new News(3L, "news Ukraine title third three",
                            "text of the third news about Ukraine", "username3",
                            LocalDateTime.parse("2023-04-22T09:23:07.319"),
                            new ArrayList<>()),
                    new News(5L, "news Ukraine title fifth five",
                            "text of the fifth news about Ukraine", "username5",
                            LocalDateTime.parse("2023-04-12T18:55:07.319"),
                            new ArrayList<>()));
            Pageable pageable = PageRequest.of(0, 2);
            News news = new News();
            news.setText("news about Ukraine");
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnoreNullValues()
                    .withMatcher("title", contains().ignoreCase(true))
                    .withMatcher("text", contains().ignoreCase(true));
            Example<News> example = Example.of(news, matcher);

            //when
            List<News> actual = newsRepository.findAll(example, pageable).toList();

            //then
            assertEquals(expected, actual);
        }

        @Test
        void findAllTest_shouldReturnNewsWithPaginationAndWithPartOfTitleAndPartOfTextFiltering() {
            //given
            List<News> expected = List.of(
                    new News(2L, "news AI title second two",
                            "text of the second news about AI", "username2",
                            LocalDateTime.parse("2023-04-12T14:43:07.319"),
                            new ArrayList<>()),
                    new News(4L, "news AI title fourth four",
                            "text of the fourth news about AI", "username4",
                            LocalDateTime.parse("2023-04-17T11:34:07.319"),
                            new ArrayList<>()));
            Pageable pageable = PageRequest.of(0, 2);
            News news = new News();
            news.setText("about AI");
            news.setTitle("AI title");
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnoreNullValues()
                    .withMatcher("title", contains().ignoreCase(true))
                    .withMatcher("text", contains().ignoreCase(true));
            Example<News> example = Example.of(news, matcher);

            //when
            List<News> actual = newsRepository.findAll(example, pageable).toList();

            //then
            assertEquals(expected, actual);
        }

        @Test
        void findAllTest_shouldReturnEmptyListWithPartOfTitleAndPartOfTextFiltering() {
            //given
            List<News> expected = new ArrayList<>();
            Pageable pageable = PageRequest.of(0, 3);
            News news = new News();
            news.setText("nonexistent Text");
            news.setTitle("nonexistent Title");
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnoreNullValues()
                    .withMatcher("title", contains().ignoreCase(true))
                    .withMatcher("text", contains().ignoreCase(true));
            Example<News> example = Example.of(news, matcher);

            //when
            List<News> actual = newsRepository.findAll(example, pageable).toList();

            //then
            assertEquals(expected, actual);
        }
    }
}