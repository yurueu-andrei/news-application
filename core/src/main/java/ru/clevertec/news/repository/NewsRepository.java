package ru.clevertec.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.news.entity.News;

/**
 * JPA repository for News.
 *
 * @author Yuryeu Andrei
 * @see JpaRepository
 * @see News
 */
public interface NewsRepository extends JpaRepository<News, Long> {

}
