package ru.clevertec.news.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.handling.exception.ServiceException;
import ru.clevertec.logging.annotation.Logging;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.NewsListDto;
import ru.clevertec.news.dto.NewsSaveDto;
import ru.clevertec.news.dto.NewsUpdateDto;
import ru.clevertec.news.dto.filter.NewsRequestFilter;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.mapper.NewsMapper;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.repository.NewsRepository;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

/**
 * Service class for news with <b>CRUD</b> operations and wrapping into <b>DTO</b>.
 *
 * @author Yuryeu Andrei
 * @see NewsRepository
 * @see NewsMapper
 */
@Service
@RequiredArgsConstructor
@Logging
public class NewsService {

    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final NewsMapper newsMapper;

    /**
     * Method for finding News by its ID
     *
     * @param id ID of target entity
     * @return returns a <b>DTO</b> made out of found News
     * @throws ServiceException in case of <b>null</b> ID and if News not found
     * @see NewsDto
     */
    @Transactional(readOnly = true)
    public NewsDto findById(Long id) throws ServiceException {
        return newsRepository.findById(id).map(newsMapper::toDto)
                .orElseThrow(() ->
                        new ServiceException("The piece of news with id = " + id + " was not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Method for finding all News with filtering by part of title and text and pagination
     *
     * @param filter POJO with values needed for filtering
     * @return returns a <b>list of DTOs</b> made out of found News
     * @see NewsListDto
     */
    @Transactional(readOnly = true)
    public List<NewsListDto> findAll(NewsRequestFilter filter) throws ServiceException {
        try {
            List<News> news = newsRepository.findAll(
                    constructExample(filter),
                    filter.getPageable()
            ).toList();
            return newsMapper.toListDto(news);
        } catch (Exception ex) {
            throw new ServiceException("The news were not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Private method creating Example instance for filtering
     *
     * @param filter filter class, containing part of text and part of title(both may be null)
     * @return <b>Example</b> - ready to use example for QBE(query by example)
     * @see Example
     */
    private Example<News> constructExample(NewsRequestFilter filter) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("title", contains().ignoreCase(true))
                .withMatcher("text", contains().ignoreCase(true));
        News news = new News();
        news.setTitle(filter.getPartOfTitle());
        news.setText(filter.getPartOfText());
        return Example.of(news, matcher);
    }

    /**
     * Method for adding a new News
     *
     * @param newsSaveDto saveDTO with fields needed for add
     * @return returns a DTO of added News<b>(contains generated ID)</b>
     * @throws ServiceException in case of <b>null</b> saveDTO
     * @see NewsDto
     * @see NewsSaveDto
     */
    @Transactional
    public NewsDto add(NewsSaveDto newsSaveDto) throws ServiceException {
        try {
            News news = newsMapper.fromSaveDto(newsSaveDto);
            return newsMapper.toDto(newsRepository.save(news));
        } catch (Exception ex) {
            throw new ServiceException("A piece of news was not added", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Method for updating the existing News
     *
     * @param newsUpdateDto DTO object with target ID and field(s) <b>to be updated</b>
     * @return <b>NewsDto</b> with updated fields
     * @throws ServiceException in case of update of nonexistent News
     * @see NewsUpdateDto
     */
    @Transactional
    public NewsDto update(NewsUpdateDto newsUpdateDto) throws ServiceException {
        try {
            News news = newsRepository.findById(newsUpdateDto.getId())
                    .orElseThrow(() ->
                            new ServiceException("The piece of news to update was not found. Id = " + newsUpdateDto.getId(),
                                    HttpStatus.NOT_FOUND));
            settingUpdatedFields(news, newsUpdateDto);
            newsRepository.flush();
            return newsMapper.toDto(news);
        } catch (Exception ex) {
            throw new ServiceException("A piece of news with id = " + newsUpdateDto.getId() + " was not updated",
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Private method for setting fields values of DTO into found News
     *
     * @param newsUpdateDto DTO object with field(s) to be inserted into News to be updated
     * @param news          News to be updated
     */
    private void settingUpdatedFields(
            News news,
            NewsUpdateDto newsUpdateDto
    ) {
        if (newsUpdateDto.getTitle() != null) {
            news.setTitle(newsUpdateDto.getTitle());
        }
        if (newsUpdateDto.getText() != null) {
            news.setText(newsUpdateDto.getText());
        }
    }

    /**
     * Method for deleting a News
     *
     * @param id ID of target News
     * @return <b>true</b> - in case of successful deletion
     * @throws ServiceException in case of <b>null</b> ID
     */
    @Transactional
    public boolean delete(Long id) {
        try {
            commentRepository.deleteAllByNewsId(id);
            newsRepository.deleteById(id);
            return true;
        } catch (Exception ex) {
            throw new ServiceException("The piece of news with id = " + id + " was not deleted", HttpStatus.BAD_REQUEST);
        }
    }
}
