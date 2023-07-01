package ru.clevertec.security.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.security.entity.Role;
import ru.clevertec.security.entity.User;

import java.util.Optional;

@SpringBootTest
@Testcontainers
class UserRepositoryTest {

    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testPostgres")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    static void start() {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    @Autowired
    private UserRepository userRepository;

    @Nested
    class FindByUsernameTest {

        @Test
        void findByUsernameTest_shouldReturnUserWithGivenUsername() {
            //given
            String username = "dobrowydka";
            Optional<User> expected = Optional.of(
                    new User(1L, "Andrei",
                            "Yurueu", "dobrowydka",
                            "$2a$10$1thhI4HpguI4pcK4WNTtKeUOl5B1NyLNa2mpuh/yBBf9INlsmRE8q",
                            Role.ADMIN));

            //when
            Optional<User> actual = userRepository.findByUsername(username);

            //then
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void findByUsernameTest_shouldReturnEmptyOptionalInCaseOfUserNotFound() {
            //given
            String username = "000000000000000000000";
            Optional<User> expected = Optional.empty();

            //when
            Optional<User> actual = userRepository.findByUsername(username);

            //then
            Assertions.assertEquals(expected, actual);
        }
    }
}