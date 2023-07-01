package ru.clevertec.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.CommentListDto;
import ru.clevertec.news.dto.CommentSaveDto;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.entity.News;

import java.util.List;

/**
 * Mapper class for comments with <b>toDto()</b>, <b>fromSaveDto()</b> and <b>toListDto()</b> methods
 * for transferring DTO object to Entity object and vice-a-versa.
 *
 * @author Yuryeu Andrei
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    CommentDto toDto(Comment comment);

    List<CommentListDto> toListDto(List<Comment> comment);

    default Comment fromSaveDto(CommentSaveDto commentSaveDto) {
        if (commentSaveDto == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setText(commentSaveDto.getText());
        comment.setUsername(commentSaveDto.getUsername());
        News news = new News();
        news.setId(commentSaveDto.getNewsId());
        comment.setNews(news);

        return comment;
    }
}
