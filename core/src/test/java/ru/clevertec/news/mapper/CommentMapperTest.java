package ru.clevertec.news.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.CommentListDto;
import ru.clevertec.news.dto.CommentSaveDto;
import ru.clevertec.news.dto.NewsListDto;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.entity.News;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class CommentMapperTest {

    private CommentMapper commentMapper;

    @BeforeEach
    void setUp() {
        this.commentMapper = Mappers.getMapper(CommentMapper.class);
    }

    @Nested
    class toDto {

        @Test
        void toDtoTest_shouldTransferCommentObjectToCommentDto() {
            //given
            LocalDateTime date = LocalDateTime.now();
            CommentDto expected = new CommentDto(1L, "text", "username", date, new NewsListDto());
            Comment comment = new Comment(1L, "text", "username", date, new News());

            //when
            CommentDto actual = commentMapper.toDto(comment);

            //then
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void toDtoTest_shouldTransferCommentObjectToCommentDtoWithNotEmptyNews() {
            //given
            LocalDateTime date = LocalDateTime.now();
            CommentDto expected = new CommentDto(1L, "text", "username", date, new NewsListDto(1L, "title", "username", date));
            Comment comment = new Comment(1L, "text", "username", date, new News(1L, "title", "text", "username", date, new ArrayList<>()));

            //when
            CommentDto actual = commentMapper.toDto(comment);

            //then
            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    void toListDtoTest_shouldTransferListOfCommentsToListOfCommentListDto() {
        //given
        LocalDateTime date = LocalDateTime.now();
        List<CommentListDto> expected = List.of(
                new CommentListDto(1L, "text1", "username1", date),
                new CommentListDto(2L, "text2", "username2", date),
                new CommentListDto(3L, "text3", "username3", date));
        List<Comment> news = List.of(
                new Comment(1L, "text1", "username1", date, new News()),
                new Comment(2L, "text2", "username2", date, new News()),
                new Comment(3L, "text3", "username3", date, new News()));

        //when
        List<CommentListDto> actual = commentMapper.toListDto(news);

        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void fromSaveDtoTest_shouldTransferCommentSaveDtoToCommentObject() {
        //given
        CommentSaveDto commentSaveDto = new CommentSaveDto("text", "username", 3L);
        News news = new News();
        news.setId(3L);
        Comment expected = new Comment(null, "text", "username", null, news);

        //when
        Comment actual = commentMapper.fromSaveDto(commentSaveDto);

        //then
        Assertions.assertEquals(expected, actual);
    }
}