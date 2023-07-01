package ru.clevertec.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.NewsListDto;
import ru.clevertec.news.dto.NewsSaveDto;
import ru.clevertec.news.entity.News;

import java.util.List;

/**
 * Mapper class for news with <b>toDto()</b>, <b>fromSaveDto()</b> and <b>toListDto()</b> methods
 * for transferring DTO object to Entity object and vice-a-versa.
 *
 * @author Yuryeu Andrei
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {

    NewsDto toDto(News news);

    List<NewsListDto> toListDto(List<News> news);

    News fromSaveDto(NewsSaveDto newsSaveDto);
}
