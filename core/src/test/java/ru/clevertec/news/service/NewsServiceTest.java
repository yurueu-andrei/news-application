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
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.NewsListDto;
import ru.clevertec.news.dto.NewsSaveDto;
import ru.clevertec.news.dto.NewsUpdateDto;
import ru.clevertec.news.dto.filter.NewsRequestFilter;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.mapper.NewsMapper;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.repository.NewsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private CommentRepository commentRepository;

    @Spy
    private NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);

    @InjectMocks
    private NewsService newsService;

    @Nested
    class FindById {

        @Test
        void findByIdTest_shouldReturnNewsDtoWithId1() {
            //given
            Long id = 1L;
            News news = new News(1L, "news crypto title first one",
                    "text of the first news about crypto", "username",
                    LocalDateTime.parse("2023-04-02T17:26:07.319"),
                    new ArrayList<>());
            NewsDto expected = new NewsDto(news.getId(), news.getTitle(), news.getText(), news.getUsername(), news.getCreateDate());

            //when
            when(newsRepository.findById(id)).thenReturn(Optional.of(news));
            NewsDto actual = newsService.findById(id);

            //then
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void findByIdTest_shouldThrowServiceExceptionForNonExistentNews() {
            //given
            Long id = 55L;

            //when
            when(newsRepository.findById(id)).thenReturn(Optional.empty());

            //then
            Assertions.assertThrows(ServiceException.class, () -> newsService.findById(id));
        }
    }

    @Test
    void findAllTest_shouldReturnAllNewsWithIdFrom4To6() {
        //given
        Pageable pageable = PageRequest.of(1, 3);
        List<News> news = List.of(
                new News(4L, "news AI title fourth four",
                        "text of the fourth news about AI", "username",
                        LocalDateTime.parse("2023-04-17T11:34:07.319"),
                        new ArrayList<>()),
                new News(5L, "news Ukraine title fifth five",
                        "text of the fifth news about Ukraine", "username",
                        LocalDateTime.parse("2023-04-12T18:55:07.319"),
                        new ArrayList<>()),
                new News(6L, "news crypto title sixth six",
                        "text of the sixth news about crypto", "username",
                        LocalDateTime.parse("2023-04-18T11:23:07.319"),
                        new ArrayList<>()));
        List<NewsListDto> expected = news
                .stream()
                .map(e -> new NewsListDto(e.getId(), e.getTitle(), e.getUsername(), e.getCreateDate()))
                .toList();

        NewsRequestFilter filter = new NewsRequestFilter();
        filter.setPageable(pageable);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("title", contains().ignoreCase(true))
                .withMatcher("text", contains().ignoreCase(true));
        News forExample = new News();
        forExample.setTitle(filter.getPartOfTitle());
        forExample.setText(filter.getPartOfText());
        Example<News> example = Example.of(forExample, matcher);

        //when
        when(newsRepository.findAll(example, pageable)).thenReturn(new PageImpl<>(news));
        List<NewsListDto> actual = newsService.findAll(filter);

        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void addTest_shouldAddTagAndReturnNewsDto() {
        //given
        LocalDateTime date = LocalDateTime.now();
        News newsWithoutId = new News(null, "hello", "hi", "username", null, new ArrayList<>());
        News newsWithId = new News(11L, "hello", "hi", "username", date, new ArrayList<>());
        NewsDto expected = new NewsDto(11L, "hello", "hi", "username", date);
        NewsSaveDto newsSaveDto = new NewsSaveDto("hello", "hi", "username");

        //when
        when(newsRepository.save(newsWithoutId)).thenReturn(newsWithId);
        NewsDto actual = newsService.add(newsSaveDto);

        //then
        Assertions.assertEquals(expected, actual);
    }

    @Nested
    class Update {

        @Test
        void updateTest_shouldUpdateNewsAndReturnNewsDtoInCaseOfSuccessfulUpdate() {
            //given
            Long id = 3L;
            News news = new News(3L, "news Ukraine title third three",
                    "text of the third news about Ukraine", "username",
                    LocalDateTime.parse("2023-04-22T09:23:07.319"),
                    new ArrayList<>());
            NewsUpdateDto newsUpdateDto = new NewsUpdateDto(3L, "hello", "hello");
            NewsDto expected = new NewsDto(3L, "hello", "hello", "username", news.getCreateDate());

            //when
            when(newsRepository.findById(id)).thenReturn(Optional.of(news));
            NewsDto actual = newsService.update(newsUpdateDto);

            //then
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void updateTest_shouldThrowServiceExceptionInCaseOfUnsuccessfulUpdate() {
            //given
            Long id = 3L;
            NewsUpdateDto newsUpdateDto = new NewsUpdateDto(3L, "hello", "hello");

            //when
            when(newsRepository.findById(id)).thenReturn(Optional.empty());

            //then
            Assertions.assertThrows(ServiceException.class, () -> newsService.update(newsUpdateDto));
        }
    }

    @Test
    void deleteTest_shouldReturnTrueInCaseOfSuccessfulDelete() {
        //given
        Long id = 3L;

        //when
        boolean actual = newsService.delete(id);

        //then
        Mockito.verify(commentRepository, Mockito.times(1))
                .deleteAllByNewsId(Mockito.any());
        Mockito.verify(newsRepository, Mockito.times(1))
                .deleteById(Mockito.any());
        Assertions.assertTrue(actual);
    }
}