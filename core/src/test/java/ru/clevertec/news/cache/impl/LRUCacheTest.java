package ru.clevertec.news.cache.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.entity.News;

import java.time.LocalDateTime;
import java.util.List;

class LRUCacheTest {

    private static LRUCache cache = new LRUCache(3);

    @BeforeEach
    void setUp() {
        cache = new LRUCache(3);
        cache.set(1L, new News(1L, "title1", "text1", "username1", LocalDateTime.of(2023, 5, 1, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null))));
        cache.set(2L, new News(2L, "title2", "text2", "username2", LocalDateTime.of(2023, 5, 2, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null), new Comment(2L, "text2", "username2", LocalDateTime.now(), null))));
    }

    @Test
    void checkGetShouldReturnEntityWithIdEqualTo1() {
        //given
        News expected = new News(1L, "title1", "text1", "username1", LocalDateTime.of(2023, 5, 1, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null)));

        //when
        Object actual = cache.get(1L);

        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkGetAllShouldReturnEntitiesWithIdsEqualTo1And2() {
        //given
        List<News> expected = List.of(
                new News(1L, "title1", "text1", "username1", LocalDateTime.of(2023, 5, 1, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null))),
                new News(2L, "title2", "text2", "username2", LocalDateTime.of(2023, 5, 2, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null), new Comment(2L, "text2", "username2", LocalDateTime.now(), null)))
        );

        //when
        List<Object> actual = cache.getAll();

        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkSetShouldAddThirdEntityToCache() {
        //given
        List<News> expected = List.of(
                new News(1L, "title1", "text1", "username1", LocalDateTime.of(2023, 5, 1, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null))),
                new News(2L, "title2", "text2", "username2", LocalDateTime.of(2023, 5, 2, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null), new Comment(2L, "text2", "username2", LocalDateTime.now(), null))),
                new News(3L, "title3", "text3", "username3", LocalDateTime.of(2023, 5, 3, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null), new Comment(3L, "text3", "username3", LocalDateTime.now(), null)))
        );

        //when
        cache.set(3L, new News(3L, "title3", "text3", "username3", LocalDateTime.of(2023, 5, 3, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null), new Comment(3L, "text3", "username3", LocalDateTime.now(), null))));
        List<Object> actual = cache.getAll();

        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkSetShouldRemoveTheOldestEntityToGetCapacityForNewOneAndAddNewEntity() {
        //given
        List<News> expected = List.of(
                new News(2L, "title2", "text2", "username2", LocalDateTime.of(2023, 5, 2, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null), new Comment(2L, "text2", "username2", LocalDateTime.now(), null))),
                new News(3L, "title3", "text3", "username3", LocalDateTime.of(2023, 5, 3, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null), new Comment(3L, "text3", "username3", LocalDateTime.now(), null))),
                new News(4L, "title4", "text4", "username4", LocalDateTime.of(2023, 5, 4, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null), new Comment(3L, "text3", "username3", LocalDateTime.now(), null), new Comment(4L, "text4", "username4", LocalDateTime.now(), null)))
        );
        cache.set(3L, new News(3L, "title3", "text3", "username3", LocalDateTime.of(2023, 5, 3, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null), new Comment(3L, "text3", "username3", LocalDateTime.now(), null))));

        //when
        cache.set(4L, new News(4L, "title4", "text4", "username4", LocalDateTime.of(2023, 5, 4, 0, 0, 0), List.of(new Comment(1L, "text1", "username1", LocalDateTime.now(), null), new Comment(3L, "text3", "username3", LocalDateTime.now(), null), new Comment(4L, "text4", "username4", LocalDateTime.now(), null))));
        List<Object> actual = cache.getAll();

        //then
        Assertions.assertEquals(expected, actual);

    }

    @Test
    void checkDeleteShouldDeleteEntityFromCache() {
        //given
        int sizeBeforeDelete = cache.getAll().size();

        //when
        cache.delete(1L);
        int sizeAfterDelete = cache.getAll().size();

        //then
        Assertions.assertNotEquals(sizeBeforeDelete, sizeAfterDelete);
    }
}