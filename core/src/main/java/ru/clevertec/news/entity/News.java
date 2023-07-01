package ru.clevertec.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * News entity
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "username")
    private String username;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @PrePersist
    public void createDate() {
        setCreateDate(LocalDateTime.now());
    }
}
