package ru.clevertec.news.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.NewsListDto;
import ru.clevertec.news.dto.NewsSaveDto;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.entity.News;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class NewsMapperTest {

    private NewsMapper newsMapper;

    @BeforeEach
    void setUp() {
        this.newsMapper = Mappers.getMapper(NewsMapper.class);
    }

    @Nested
    class toDto {

        @Test
        void toDtoTest_shouldTransferNewsObjectToNewsDto() {
            //given
            LocalDateTime date = LocalDateTime.now();
            NewsDto expected = new NewsDto(1L, "title", "text", "username", date);
            News news = new News(1L, "title", "text", "username", date, new ArrayList<>());

            //when
            NewsDto actual = newsMapper.toDto(news);

            //then
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void toDtoTest_shouldTransferNewsObjectToNewsDtoWithNotEmptyCollection() {
            //given
            LocalDateTime date = LocalDateTime.now();
            NewsDto expected = new NewsDto(1L, "title", "text", "username", date);
            News news = new News(1L, "title", "text", "username", date, List.of(new Comment(1L, "text", "username", date, new News())));

            //when
            NewsDto actual = newsMapper.toDto(news);

            //then
            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    void toListDtoTest_shouldTransferListOfNewsToListOfNewsListDto() {
        //given
        LocalDateTime date = LocalDateTime.now();
        List<NewsListDto> expected = List.of(
                new NewsListDto(1L, "title1", "username", date.plusDays(1)),
                new NewsListDto(2L, "title2", "username", date.plusDays(2)),
                new NewsListDto(3L, "title3", "username", date.plusDays(3)));
        List<News> news = List.of(
                new News(1L, "title1", "text1", "username", date.plusDays(1), new ArrayList<>()),
                new News(2L, "title2", "text2", "username", date.plusDays(2), new ArrayList<>()),
                new News(3L, "title3", "text3", "username", date.plusDays(3), new ArrayList<>()));

        //when
        List<NewsListDto> actual = newsMapper.toListDto(news);

        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void fromSaveDtoTest_shouldTransferNewsSaveDtoToNewsObject() {
        //given
        NewsSaveDto newsSaveDto = new NewsSaveDto("title", "text", "username");
        News expected = new News(null, "title", "text", "username", null, null);

        //when
        News actual = newsMapper.fromSaveDto(newsSaveDto);

        //then
        Assertions.assertEquals(expected, actual);
    }
}