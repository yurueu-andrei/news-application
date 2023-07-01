package ru.clevertec.news.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.news.entity.Comment;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for Comments.
 *
 * @author Yuryeu Andrei
 * @see JpaRepository
 * @see Comment
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
                FROM Comment c
                LEFT JOIN FETCH c.news
                WHERE c.id = :id
            """
    )
    Optional<Comment> findById(Long id);

    List<Comment> findAllByNewsId(Long newsId, Pageable pageable);

    @Modifying
    @Query("""
                DELETE FROM Comment c
                WHERE c.news.id = :id
            """
    )
    void deleteAllByNewsId(Long id);
}
